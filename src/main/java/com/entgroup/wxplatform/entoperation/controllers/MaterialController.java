package com.entgroup.wxplatform.entoperation.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 素材管理
 * @author wuyongwen
 * @Date 2016年3月8日下午2:28:24
 */
public class MaterialController {
	@RequestMapping("/materialIndex")
	public String materialCount(Model model){
		UserDetails userDetails = (UserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		return null;
	}
}
