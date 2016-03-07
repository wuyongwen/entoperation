package com.entgroup.wxplatform.entoperation;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.chn.wx.api.PlatFormManager;
import com.chn.wx.api.PlatFormTokenAccessor;
import com.chn.wx.vo.result.PlatFormAccessTokenResult;
import com.chn.wx.vo.result.PlatFormGetAuthAccessResult;
import com.chn.wx.vo.result.PlatFormGetAuthInfoResult;
import com.chn.wx.vo.result.PlatFormGetAuthorizerInfoResult;

public class PlatFormManagerTest {
	String authCode = "queryauthcode@@@YycvieQxYRo1v7PNUOib8ZSQezZOBWoneijnQOMHkZkAY5Qo6n1d6CsIs8QxOzz4mTlXzYc7NqLSIzKWG9zzkA";
	String ticket = "ticket@@@rfxKo8Sh6aT76DWorfEt5h2rHepQ99i4DfTtY5i2fFJW-WSA-IcN5XElGGVWitwnAbSgnYzNmS10yRi1Bpc2nA";
	//@Test
	public void testPlatFormAuth(){
		PlatFormTokenAccessor.getAccessToken();
	}
	//@Test
	public void testAuthInfoResult(){
		PlatFormTokenAccessor.updatePlatFormVerifyTicket(ticket);
		PlatFormGetAuthInfoResult authInfo = PlatFormManager.getAuthInfo(authCode);
		System.out.println(JSON.toJSONString(authInfo));
	}
	//@Test
	public void testGtAuthAccessToken(){
		PlatFormTokenAccessor.updatePlatFormVerifyTicket(ticket);
		PlatFormGetAuthAccessResult accessToken = PlatFormManager.getAuthAccessToken("wx54ffbc02f0621c57", "refreshtoken@@@dBYXdJhdJHHdYFD9sEvMUgICEIAHvoo7s4at4gRLeYE");
		System.out.println(JSON.toJSONString(accessToken));
	}
	//@Test
	public void testgetAuthorizerInfo(){
		PlatFormTokenAccessor.updatePlatFormVerifyTicket(ticket);
		PlatFormGetAuthorizerInfoResult authInfo = PlatFormManager.getAuthorizerInfo("wx54ffbc02f0621c57");
		System.out.println(JSON.toJSONString(authInfo));
	}
}
