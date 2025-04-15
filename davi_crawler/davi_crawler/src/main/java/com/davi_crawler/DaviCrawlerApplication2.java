package com.davi_crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class DaviCrawlerApplication2 {
    public static void main(String[] args) {
        System.setProperty("server.port", "8383");
        SpringApplication.run(DaviCrawlerApplication.class, args);
    }
}