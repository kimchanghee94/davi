package com.davi_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class DaviServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaviServerApplication.class, args);
	}

}
