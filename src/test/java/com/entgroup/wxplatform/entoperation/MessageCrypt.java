package com.entgroup.wxplatform.entoperation;

import org.junit.Test;

import com.chn.common.IOUtils;
import com.chn.wx.dto.App;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

public class MessageCrypt{
	@Test
	public void testCrypt() throws AesException, Exception{
		String msgSignature = "c316f62548058dfcc1698e25155ac31b91cbcf07";
		String timeStamp = "1456969333";
		String nonce = "568413140";
		WXBizMsgCrypt crypt = new WXBizMsgCrypt(App.Info.token, App.Info.aesKey, App.Info.id);
		String xml = IOUtils.toString(MessageCrypt.class.getResourceAsStream("/msg.xml"), "utf-8");
		String decryptXml = crypt.decryptMsg(msgSignature, timeStamp, nonce, xml);
		System.out.println(decryptXml);
	}
}
