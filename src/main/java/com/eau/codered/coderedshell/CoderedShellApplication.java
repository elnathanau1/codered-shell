package com.eau.codered.coderedshell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoderedShellApplication {
	private static Logger logger = LoggerFactory.getLogger(CoderedShellApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CoderedShellApplication.class, args);
		logger.info("Application has started");
	}

}
