package com.entgroup.wxplatform.entoperation.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chn.common.HttpUtils;
import com.chn.common.IOUtils;
import com.chn.common.StringUtils;
import com.chn.wx.MessageHandler;
import com.chn.wx.api.PlatFormManager;
import com.chn.wx.api.PlatFormTokenAccessor;
import com.chn.wx.dto.App;
import com.chn.wx.dto.Context;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

/**
 * 微信平台请求处理Controller
 * 
 * @author wuyongwen
 * @Date 2016年3月2日上午10:30:59
 */
@Controller
public class WechatPlatformController {
	public static Logger log = LoggerFactory.getLogger(WechatPlatformController.class);

	@RequestMapping(value = "/wxmsg", method = RequestMethod.POST)
	public String verifyTicket(HttpServletRequest request) {
		String signature = request.getParameter("signature");
		String nonce = request.getParameter("nonce");
		String timestamp = request.getParameter("timestamp");
		String msgSignature = request.getParameter("msg_signature");
		Context context = new Context(HttpUtils.decodeParams(request));
		String xmlContent;
		try {
			xmlContent = IOUtils.toString(request.getInputStream(), "UTF-8");
			Document document = DocumentHelper.parseText(xmlContent);
			Element root = document.getRootElement();
			for (Iterator<?> it = root.elementIterator(); it.hasNext();) {
				Element ele = (Element) it.next();
				context.addAttribute(ele.getName(), ele.getText());
			}
			WXBizMsgCrypt msgCrypt = new WXBizMsgCrypt(App.Info.token, App.Info.aesKey, App.Info.id);
			String ticket = msgCrypt.decryptMsg(msgSignature, timestamp, nonce,
					context.getAttribute("ComponentVerifyTicket").toString());

			if (!StringUtils.isEmpty(ticket))
				PlatFormTokenAccessor.updatePlatFormVerifyTicket(ticket);
			log.info(context.toString());
			log.info(signature);
			log.info(ticket);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "seccess";
	}

	@RequestMapping("/auth")
	public String auth(Model model) {
		String url = PlatFormManager.getLoginUrl();
		model.addAttribute("url", url);
		return "auth";
	}

	@RequestMapping("/callback")
	public String callback(HttpServletRequest req) {
		Context context = new Context(HttpUtils.decodeParams(req));
		log.info(context.toString());
		return "callback";
	}

	@Autowired
	MessageHandler messageHandler;

	@RequestMapping("/wx")
	public void wechat(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		Context context = new Context(HttpUtils.decodeParams(req));
		context.setAttribute("method", req.getMethod());
		if (req.getMethod().equalsIgnoreCase("POST"))
			context.setAttribute("xmlContent", HttpUtils.read(req));
		OutputStream os = resp.getOutputStream();
		log.info(context.toString());
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
