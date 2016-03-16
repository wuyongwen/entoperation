package com.entgroup.wxplatform.entoperation.controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.baidu.ueditor.ConfigManager;
import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.ActionMap;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.MultiState;
import com.baidu.ueditor.define.State;
import com.baidu.ueditor.upload.StorageManager;
import com.baidu.ueditor.upload.Uploader;
import com.chn.common.HttpUtils;
import com.chn.common.IOUtils;
import com.chn.common.StringUtils;
import com.chn.wx.api.PlatFormManager;
import com.chn.wx.vo.result.BasicResult;
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
import me.chanjar.weixin.mp.bean.WxMpMaterialNews;
import me.chanjar.weixin.mp.bean.WxMpMaterialNewsArticle;
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
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = usersvr.findByUsername(userDetails.getUsername());
		model.addAttribute("user", user);
		return "ueditor/articlenew";
	}

	/**
	 * ueditor内上传本地图片
	 * 
	 * @param file
	 * @param response
	 * @throws WxErrorException
	 * @throws IOException
	 */
	@RequestMapping("upload")
	public void uploadFile(@RequestParam(value = "upfile") MultipartFile file, HttpServletResponse response)
			throws WxErrorException, IOException {
		initTokenConfig("wx34dcba427f62a33e");
		State stat = null;
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		if (!file.isEmpty()) {
			String name = file.getOriginalFilename();
			String fileType = name.substring(name.lastIndexOf('.'));
			File tempFile = FileUtils.createTmpFile(file.getInputStream(), UUID.randomUUID().toString(), fileType);

			String rest = wxMpService.imageUpload(tempFile);
			String val = JSON.parseObject(rest).getString("url");
			if (!StringUtils.isEmpty(val)) {
				stat = new BaseState(true);
				stat.putInfo("url", val);
				stat.putInfo("type", fileType);
				stat.putInfo("original", name);
				out.write(stat.toJSONString());
			} else {
				out.write(new BaseState(false, "微信上传文件错误").toJSONString());
			}
		} else {
			out.write(new BaseState(false, "文件为空").toJSONString());
		}
		out.close();
	}

	@RequestMapping(value = "editorSyn", method = RequestMethod.POST)
	@ResponseBody
	public BasicResult editorSyn(WxMpMaterialNewsArticle article, String appId,HttpServletRequest request)
			throws WxErrorException, IOException {
		String[] appIds = appId.split(",");
		String filePath = request.getServletContext().getRealPath("/") +"views" + article.getThumbMediaId();
		File file = new File(filePath);
		String name = file.getName();
		List<WxMpMaterialUploadResult> ress = new ArrayList<WxMpMaterialUploadResult>();
		for (String s : appIds) {
			initTokenConfig(s);
			WxMpMaterial wxMaterial = new WxMpMaterial();
		    wxMaterial.setFile(file);
		    wxMaterial.setName(name);
		    WxMpMaterialUploadResult res = wxMpService.materialFileUpload(WxConsts.MEDIA_IMAGE, wxMaterial);
			WxMpMaterialNews wxMpMaterialNewsSingle = new WxMpMaterialNews();
			wxMpMaterialNewsSingle.addArticle(article);
			article.setThumbMediaId(res.getMediaId());
			WxMpMaterialUploadResult resSingle = wxMpService.materialNewsUpload(wxMpMaterialNewsSingle);
		}
		BasicResult rs = new BasicResult();
		rs.setErrmsg(ress.toString());
		return rs;
	}
	@RequestMapping(value = "uploadMaterial")
	@ResponseBody
	public String uploadMaterial(MultipartFile file,HttpServletRequest request) throws IOException, WxErrorException {
		String rootPath = request.getServletContext().getRealPath("/") +"views";
		ConfigManager confMgr = ConfigManager.getInstance(rootPath, request.getContextPath(), request.getRequestURI() );
		Map<String, Object> conf = confMgr.getConfig( ActionMap.UPLOAD_IMAGE );
		//State state = new Uploader( request, conf ).doExec();
		//FileInputStream fileStream = new FileInputStream(tempFile);
		String savePath = (String) conf.get("savePath");
		String originFileName = file.getOriginalFilename();
		String suffix = FileType.getSuffixByFilename(originFileName);

		originFileName = originFileName.substring(0,
				originFileName.length() - suffix.length());
		savePath = savePath + suffix;

		long maxSize = ((Long) conf.get("maxSize")).longValue();


		savePath = PathFormat.parse(savePath, originFileName);

		String physicalPath = (String) conf.get("rootPath") + savePath;

		InputStream is = file.getInputStream();
		State storageState = StorageManager.saveFileByInputStream(is,
				physicalPath, maxSize);
		is.close();

		if (storageState.isSuccess()) {
			storageState.putInfo("url", PathFormat.format(savePath));
			storageState.putInfo("type", suffix);
			storageState.putInfo("original", originFileName + suffix);
		}

		return storageState.toJSONString();
	}
	
	@RequestMapping(value="uploadCatcherImg",method=RequestMethod.POST)
	@ResponseBody
	public String uploadCatcherImg(HttpServletRequest request) throws WxErrorException{
		String rootPath = request.getServletContext().getRealPath("/") +"views";
		ConfigManager confMgr = ConfigManager.getInstance(rootPath, request.getContextPath(), request.getRequestURI() );
		Map<String, Object> conf = confMgr.getConfig( ActionMap.CATCH_IMAGE);
		String[] list = request.getParameterValues( (String)conf.get( "fieldName" ) );
		MultiState state = new MultiState( true );
		for ( String source : list) {
			state.addState( captureRemoteData( source ) );
		}
		return state.toJSONString();
	}
	private State captureRemoteData(String source) throws WxErrorException {
		File file = HttpUtils.downloadFile(source);
		initTokenConfig("wx34dcba427f62a33e");
		String rest = wxMpService.imageUpload(file);
		State state = new BaseState(true);
		state.putInfo( "size", file.length());
		state.putInfo( "title", file.getName());
		state.putInfo( "url", PathFormat.format(JSON.parseObject(rest).getString("url")) );
		state.putInfo( "source", source );
		return state;
	}

	private void initTokenConfig(String appid) {
		WxMpAPP app = mpsvr.findByAppId(appid);
		if (app.isExpriesIn()) {
			PlatFormGetAuthAccessResult token = PlatFormManager.getAuthAccessToken(app.getAppId(),
					app.getRefreshToken());
			app.setAccessToken(token.getAuthorizerAccessToken());
			app.setExpriesIn(System.currentTimeMillis() + (token.getExpiresIn() - 200) * 1000l);
			app.setRefreshToken(token.getAuthorizerRefreshToken());
			mpsvr.saveBean(app);
		}
		if (app != null) {
			WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
			config.updateAccessToken(app.getAccessToken(), app.getExpriesIn());
			wxMpService.setWxMpConfigStorage(config);
		}
	}
}
