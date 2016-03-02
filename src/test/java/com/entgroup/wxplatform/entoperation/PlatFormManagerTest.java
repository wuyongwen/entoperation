package com.entgroup.wxplatform.entoperation;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.chn.wx.api.PlatFormManager;
import com.chn.wx.vo.result.PlatFormGetAuthInfoResult;

public class PlatFormManagerTest {
	String authCode = "queryauthcode@@@CxHi2xJ8CzdaOS0mHHnS_8L41HcnkV6VB2v2md2A_V07eT3cYGEdzd2l4hBm4eroG3Qk0HoONF6JS9m7MYqJww";
	//@Test
	public void testAuthInfoResult(){
		PlatFormGetAuthInfoResult authInfo = PlatFormManager.getAuthInfo(authCode);
		System.out.println(JSON.toJSONString(authInfo));
	}
}
