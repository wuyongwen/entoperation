package com.entgroup.wxplatform.entoperation.controllers;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpMassGroupMessage;
import me.chanjar.weixin.mp.bean.result.WxMpMassSendResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.chn.common.HttpUtils;
import com.chn.common.IOUtils;
import com.chn.common.StringUtils;
import com.chn.wx.MessageHandler;
import com.chn.wx.api.PlatFormManager;
import com.chn.wx.dto.Context;
import com.chn.wx.vo.result.PlatFormGetAuthInfoResult;
import com.chn.wx.vo.result.PlatFormGetAuthorizerInfoResult;

/**
 * 微信平台请求处理Controller
 * 
 * @author wuyongwen
 * @Date 2016年3月2日上午10:30:59
 */
@Controller
public class WechatPlatformController {
	public static Logger log = LoggerFactory.getLogger(WechatPlatformController.class);
	/**
	 * 授权登陆页
	 * @param model
	 * @return
	 */
	@RequestMapping("/auth")
	public String auth(Model model) {
		String url = PlatFormManager.getLoginUrl();
		model.addAttribute("url", url);
		return "auth";
	}
	/**
	 * 授权成功回调
	 * @param code
	 * @param expires
	 * @param model
	 * @return
	 */
	@RequestMapping("/callback")
	public String callback(@RequestParam(value="auth_code") String code,@RequestParam(value="expires_in") Integer expires,Model model) {
		PlatFormGetAuthInfoResult authInfoResult = PlatFormManager.getAuthInfo(code);
		log.info("authInfo: "+JSON.toJSONString(authInfoResult));
		model.addAttribute("authInfo", JSON.toJSONString(authInfoResult));
		model.addAttribute("authToken",authInfoResult.getAuthorizationInfo().getAuthorizerAccessToken());
		PlatFormGetAuthorizerInfoResult authorizeInfo = PlatFormManager.getAuthorizerInfo(authInfoResult.getAuthorizationInfo().getAuthorizerAppId());
		log.info("authorizeInfo : {}" ,authorizeInfo);
		model.addAttribute("authorizeInfo", JSON.toJSONString(authorizeInfo));
		model.addAttribute("stat", null);
		return "callback";
	}
	@Autowired
	WxMpService wxMpService;
	/**
	 * 群发文本消息
	 * @param authToken
	 * @param msg
	 * @param model
	 * @return
	 * @throws WxErrorException
	 */
	@RequestMapping("/sendAll")
	public String sendAll(String authToken,String msg,Model model) throws WxErrorException{
		WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
		if(!StringUtils.isEmpty(authToken)){
			config.updateAccessToken(authToken, 7200);
			wxMpService.setWxMpConfigStorage(config);
			WxMpMassGroupMessage gmsg = new WxMpMassGroupMessage();
			gmsg.setMsgtype(WxConsts.MASS_MSG_TEXT);
			gmsg.setContent(msg);
			WxMpMassSendResult result = wxMpService.massGroupMessageSend(gmsg);
			model.addAttribute("stat", result);
		}
		return "callback";
	}
	
	
	@Autowired
	MessageHandler messageHandler;

	@RequestMapping(value={"/wx"})
	public void wechat(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		Context context = new Context(HttpUtils.decodeParams(req));
		context.setAttribute("method", req.getMethod());
		if (req.getMethod().equalsIgnoreCase("POST"))
			context.setAttribute("xmlContent", HttpUtils.read(req));
		OutputStream os = resp.getOutputStream();
		log.info("请求参数："+context.toString());
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
	public void singleWechat(@PathVariable String appId,HttpServletRequest req, HttpServletResponse resp) throws IOException{
		Context context = new Context(HttpUtils.decodeParams(req));
		context.setAttribute("method", req.getMethod());
		if (req.getMethod().equalsIgnoreCase("POST"))
			context.setAttribute("xmlContent", HttpUtils.read(req));
		//if (!StringUtils.isEmpty(appId))
		//	context.addAttribute("AppId", appId);
		OutputStream os = resp.getOutputStream();
		log.info("请求参数："+context.toString());
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
