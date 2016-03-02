package com.entgroup.wxplatform.entoperation.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chn.wx.listener.impl.service.end.event.ComponentVerifyTicketAdaptor;

@Controller
public class IndexController {
	protected Logger log = Logger.getLogger(IndexController.class);

	@RequestMapping("/")
	String index() {
		log.debug("index mapping!");
		return "index";
	}

	@RequestMapping(value = "/login")
	String login() {
		return "login";
	}
}
