package com.entgroup.wxplatform.entoperation.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chn.wx.MessageHandler;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;

@Configuration
public class WxBeanInit {
	@Bean
    public MessageHandler getMessageHandler() throws Exception{
    	return new MessageHandler();
    }
	@Bean
	public WxMpService getWxMpService(){
		return new WxMpServiceImpl();
	}
}
