package com.entgroup.wxplatform.entoperation.controllers;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.chn.common.HttpUtils;
import com.chn.common.IOUtils;
import com.chn.common.StringUtils;
import com.chn.wx.MessageHandler;
import com.chn.wx.api.PlatFormManager;
import com.chn.wx.dto.Context;
import com.chn.wx.store.PlatformConfigYamlStorage;
import com.chn.wx.vo.result.PlatFormGetAuthAccessResult;
import com.chn.wx.vo.result.PlatFormGetAuthInfoResult;
import com.chn.wx.vo.result.PlatFormGetAuthorizerInfoResult;
import com.entgroup.wxplatform.entoperation.domain.User;
import com.entgroup.wxplatform.entoperation.domain.WxMpAPP;
import com.entgroup.wxplatform.entoperation.domain.WxMpAPPInfo;
import com.entgroup.wxplatform.entoperation.services.UserService;
import com.entgroup.wxplatform.entoperation.services.WxMpAppService;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.fs.FileUtils;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpMassGroupMessage;
import me.chanjar.weixin.mp.bean.WxMpMaterial;
import me.chanjar.weixin.mp.bean.WxMpMaterialNews;
import me.chanjar.weixin.mp.bean.WxMpMaterialNewsArticle;
import me.chanjar.weixin.mp.bean.result.WxMpMassSendResult;
import me.chanjar.weixin.mp.bean.result.WxMpMaterialUploadResult;

/**
 * 微信平台请求处理Controller
 * 
 * @author wuyongwen
 * @Date 2016年3月2日上午10:30:59
 */
@Controller
public class WechatPlatformController {
	public static Logger log = LoggerFactory
			.getLogger(WechatPlatformController.class);

	@Autowired
	WxMpAppService mpsvr;
	@Autowired
	UserService usersvr;
	@Autowired
	WxMpService wxMpService;
	/**
	 * 授权登陆页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/auth")
	public String auth(Model model) {
		String url = PlatFormManager.getLoginUrl();
		String ticket = new PlatformConfigYamlStorage().getTicket();
		model.addAttribute("url", url);
		model.addAttribute("ticket",ticket);
		return "auth";
	}

	/**
	 * 授权成功回调
	 * 
	 * @param code
	 * @param expires
	 * @param model
	 * @return
	 */
	@RequestMapping("/callback")
	public String callback(@RequestParam(value = "auth_code") String code,
			@RequestParam(value = "expires_in") Integer expires, Model model) {
		PlatFormGetAuthInfoResult authInfoResult = PlatFormManager
				.getAuthInfo(code);
		log.info("authInfo: " + JSON.toJSONString(authInfoResult));
		UserDetails userDetails = (UserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		PlatFormGetAuthorizerInfoResult authorizeInfo = PlatFormManager
				.getAuthorizerInfo(authInfoResult.getAuthorizationInfo()
						.getAuthorizerAppId());
		
		if (mpsvr.findByAppId(authInfoResult.getAuthorizationInfo()
				.getAuthorizerAppId()) == null) {
			WxMpAPP app = new WxMpAPP();
			app.setAuthorizationInfo(authInfoResult.getAuthorizationInfo());
			User user = usersvr.findByUsername(userDetails.getUsername());
			app.setUser(user);
			WxMpAPPInfo info = new WxMpAPPInfo();
			info.setAuthorizerInfor(authorizeInfo);
			app.setInfoDetial(info);
			mpsvr.saveBean(app);
		}else{
			WxMpAPP app = mpsvr.findByAppId(authInfoResult.getAuthorizationInfo()
					.getAuthorizerAppId());
			app.setAuthorizationInfo(authInfoResult.getAuthorizationInfo());
			app.getInfoDetial().setAuthorizerInfor(authorizeInfo);
			User user = usersvr.findByUsername(userDetails.getUsername());
			app.setUser(user);
			mpsvr.saveBean(app);
		}

		model.addAttribute("authInfo", JSON.toJSONString(authInfoResult));
		model.addAttribute("authToken", authInfoResult.getAuthorizationInfo()
				.getAuthorizerAccessToken());
		log.info("authorizeInfo : {}", authorizeInfo);
		model.addAttribute("authorizeInfo", JSON.toJSONString(authorizeInfo));
		return "callback";
	}
	
	@RequestMapping("/myapp")
	public String myApp(Model model){
		UserDetails userDetails = (UserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		User user = usersvr.findByUsername(userDetails.getUsername());
		model.addAttribute("user", user);
		return "myapp";
	}
	@RequestMapping("/sendAllIndex")
	public String sendAllIndex(Model model){
		UserDetails userDetails = (UserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		User user = usersvr.findByUsername(userDetails.getUsername());
		model.addAttribute("user", user);
		return "sendAllIndex";
	}
	/**
	 * 群发文本消息
	 * 
	 * @param authToken
	 * @param msg
	 * @param model
	 * @return
	 * @throws WxErrorException
	 */
	@RequestMapping(value="/sendAll",method=RequestMethod.POST)
	public String sendAll(String appid, String msg, Model model)
			throws WxErrorException {
		beforeSend(appid, msg, model);
		WxMpMassGroupMessage gmsg = new WxMpMassGroupMessage();
		gmsg.setMsgtype(WxConsts.MASS_MSG_TEXT);
		gmsg.setContent(msg);
		try{
			WxMpMassSendResult result = wxMpService.massGroupMessageSend(gmsg);
			model.addAttribute("stat", result);
		}catch(WxErrorException e){
			model.addAttribute("error", e.getError().toString());
		}
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		User user = usersvr.findByUsername(userDetails.getUsername());
		model.addAttribute("user", user);
		
		return "sendAllIndex";
	}

	protected void beforeSend(String appid, String msg, Model model) {
		if(StringUtils.isEmpty(appid)||StringUtils.isEmpty(msg))
			model.addAttribute("error", "选择一个公众号且信息不能为空!");
		initTokenConfig(appid);
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
		}else{
			model.addAttribute("error", "公众号不存在!");
		}
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

	@RequestMapping(value="/sendAllImg",method=RequestMethod.POST)
	public String sendAllImg(Model model,String msgContent,String appid,String mediaId) throws WxErrorException{
		beforeSend(appid, msgContent, model);
		
		 // 单图文消息
	    WxMpMaterialNews wxMpMaterialNewsSingle = new WxMpMaterialNews();
	    WxMpMaterialNewsArticle mpMaterialNewsArticleSingle = new WxMpMaterialNewsArticle();
	    mpMaterialNewsArticleSingle.setAuthor("author");
	    mpMaterialNewsArticleSingle.setThumbMediaId(mediaId);
	    mpMaterialNewsArticleSingle.setTitle("single title");
	    mpMaterialNewsArticleSingle.setContent(msgContent);
	    mpMaterialNewsArticleSingle.setContentSourceUrl("content url");
	    mpMaterialNewsArticleSingle.setShowCoverPic(true);
	    mpMaterialNewsArticleSingle.setDigest("single news");
	    wxMpMaterialNewsSingle.addArticle(mpMaterialNewsArticleSingle);
	    WxMpMaterialUploadResult resSingle = wxMpService.materialNewsUpload(wxMpMaterialNewsSingle);
	    WxMpMassGroupMessage gmsg = new WxMpMassGroupMessage();
		gmsg.setMsgtype(WxConsts.MASS_MSG_NEWS);
		gmsg.setMediaId(resSingle.getMediaId());
		try{
			WxMpMassSendResult result = wxMpService.massGroupMessageSend(gmsg);
			model.addAttribute("stat", result);
		}catch(WxErrorException e){
			model.addAttribute("error", e.getError().toString());
		}
		UserDetails userDetails = (UserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		User user = usersvr.findByUsername(userDetails.getUsername());
		model.addAttribute("user", user);
		
	    return "sendAllIndex";
	}

	@RequestMapping(value = "/upload")
	@ResponseBody
	public String upload(MultipartFile file,Model model,String appId) throws IOException, WxErrorException {
		initTokenConfig("wx34dcba427f62a33e");
		if (!file.isEmpty()) { 
			String name = file.getOriginalFilename();
			String fileType = name.substring(name.lastIndexOf('.'));
			File tempFile = FileUtils.createTmpFile(file.getInputStream(), UUID.randomUUID().toString(),
					fileType);
			
			 String rest = wxMpService.imageUpload(tempFile);
			return rest;
		}
		return null;
	}
	@RequestMapping(value = "/uploadMaterial")
	@ResponseBody
	public WxMpMaterialUploadResult uploadMaterial(MultipartFile file,String appId) throws IOException, WxErrorException {
		initTokenConfig("wx34dcba427f62a33e");
		if (!file.isEmpty()) { 
			String name = file.getOriginalFilename();
			String fileType = name.substring(name.lastIndexOf('.'));
			File tempFile = FileUtils.createTmpFile(file.getInputStream(), UUID.randomUUID().toString(),
					fileType);
			WxMpMaterial wxMaterial = new WxMpMaterial();
		    wxMaterial.setFile(tempFile);
		    wxMaterial.setName(name);
		    WxMpMaterialUploadResult res = wxMpService.materialFileUpload(WxConsts.MEDIA_IMAGE, wxMaterial);
		    
			return res;
		}
		return null;
	}
	@Autowired
	MessageHandler messageHandler;
	/**
	 * 微信消息处理
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	@RequestMapping(value = { "/wx" })
	public void wechat(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		Context context = new Context(HttpUtils.decodeParams(req));
		context.setAttribute("method", req.getMethod());
		if (req.getMethod().equalsIgnoreCase("POST"))
			context.setAttribute("xmlContent", HttpUtils.read(req));
		OutputStream os = resp.getOutputStream();
		log.info("请求参数：" + context.toString());
		try {
			String responseString = messageHandler.process(context);
			os.write(StringUtils.getBytesUtf8(responseString));
		} catch (Exception e) {
			log.error("消息处理失败", e);
		} finally {
			IOUtils.closeQuietly(os);
		}
	}

	@RequestMapping("/wx/{appId}/wxmsg")
	public void singleWechat(@PathVariable String appId,
			HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Context context = new Context(HttpUtils.decodeParams(req));
		context.setAttribute("method", req.getMethod());
		if (req.getMethod().equalsIgnoreCase("POST"))
			context.setAttribute("xmlContent", HttpUtils.read(req));
		// if (!StringUtils.isEmpty(appId))
		// context.addAttribute("AppId", appId);
		OutputStream os = resp.getOutputStream();
		log.info("请求参数：" +appId+" : "+ context.toString());
		try {
			String responseString = messageHandler.process(context);
			os.write(StringUtils.getBytesUtf8(responseString));
		} catch (Exception e) {
			log.error("消息处理失败", e);
		} finally {
			IOUtils.closeQuietly(os);
		}
	}

}
