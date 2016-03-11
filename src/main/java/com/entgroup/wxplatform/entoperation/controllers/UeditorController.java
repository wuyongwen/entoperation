package com.entgroup.wxplatform.entoperation.controllers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.State;
import com.chn.common.StringUtils;
import com.chn.wx.api.PlatFormManager;
import com.chn.wx.vo.result.PlatFormGetAuthAccessResult;
import com.entgroup.wxplatform.entoperation.domain.User;
import com.entgroup.wxplatform.entoperation.domain.WxMpAPP;
import com.entgroup.wxplatform.entoperation.services.UserService;
import com.entgroup.wxplatform.entoperation.services.WxMpAppService;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.fs.FileUtils;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpMaterial;
import me.chanjar.weixin.mp.bean.result.WxMpMaterialUploadResult;

@Controller
@RequestMapping("/ueditor")
public class UeditorController {
	@Autowired
	WxMpAppService mpsvr;
	@Autowired
	UserService usersvr;
	@Autowired
	WxMpService wxMpService;
	@RequestMapping("new")
	public String ueditorNew(Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		User user = usersvr.findByUsername(userDetails.getUsername());
		model.addAttribute("user", user);
		return "ueditor/articlenew";
	}
	/**
	 * ueditor内上传本地图片
	 * @param file
	 * @param response
	 * @throws WxErrorException
	 * @throws IOException
	 */
	@RequestMapping("upload")
	public void uploadFile(@RequestParam(value="upfile") MultipartFile file,HttpServletResponse response) throws WxErrorException, IOException{
		initTokenConfig("wx34dcba427f62a33e");
		State stat = null;
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		if (!file.isEmpty()) { 
			String name = file.getOriginalFilename();
			String fileType = name.substring(name.lastIndexOf('.'));
			File tempFile = FileUtils.createTmpFile(file.getInputStream(), UUID.randomUUID().toString(),
					fileType);
			
			String rest = wxMpService.imageUpload(tempFile);
			String val  = JSON.parseObject(rest).getString("url");
			if(!StringUtils.isEmpty(val)){
				stat = new BaseState(true);
				stat.putInfo("url", val);
				stat.putInfo("type", fileType);
				stat.putInfo("original", name);
				out.write(stat.toJSONString());
			}else {
				out.write(new BaseState(false,"微信上传文件错误").toJSONString());
			}
		}else{
			out.write(new BaseState(false,"文件为空").toJSONString());
		}
		out.close();
	}
	
	private void initTokenConfig(String appid) {
		WxMpAPP app =  mpsvr.findByAppId(appid);
		if(app.isExpriesIn()){
			PlatFormGetAuthAccessResult token = PlatFormManager.getAuthAccessToken(app.getAppId(),app.getRefreshToken());
			app.setAccessToken(token.getAuthorizerAccessToken());
			app.setExpriesIn(System.currentTimeMillis()
					+ (token.getExpiresIn() - 200) * 1000l);
			app.setRefreshToken(token.getAuthorizerRefreshToken());
			mpsvr.saveBean(app);
		}
		if (app!=null) {
			WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
			config.updateAccessToken(app.getAccessToken(), app.getExpriesIn());
			wxMpService.setWxMpConfigStorage(config);
		}		
	}
}
