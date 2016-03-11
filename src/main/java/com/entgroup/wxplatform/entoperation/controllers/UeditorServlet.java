package com.entgroup.wxplatform.entoperation.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baidu.ueditor.ActionEnter;

public class UeditorServlet extends HttpServlet{

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String rootPath = request.getServletContext().getRealPath("/") +"views";
		PrintWriter out = response.getWriter();
		out.write(new ActionEnter(request, rootPath).exec());
		out.close();
	}
}
