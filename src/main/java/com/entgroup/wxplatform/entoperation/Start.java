package com.entgroup.wxplatform.entoperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;


@SpringBootApplication
public class Start extends SpringBootServletInitializer {
	static Logger log = LoggerFactory.getLogger(Start.class);
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Start.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(Start.class, args);
		log.info("springboot starting ... ");
	}
}
