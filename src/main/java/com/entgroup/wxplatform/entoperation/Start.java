package com.entgroup.wxplatform.entoperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */

@SpringBootApplication
public class Start {
	static final Logger log = LoggerFactory.getLogger(Start.class);

	public static void main(String[] args) {
		SpringApplication.run(Start.class, args);
		log.info("springboot starting ... ");
	}
}
