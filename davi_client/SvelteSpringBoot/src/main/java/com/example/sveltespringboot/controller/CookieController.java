package com.example.sveltespringboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/cookie-api")
public class CookieController {

    @RequestMapping("/createCookie")
    public String createCookie(HttpServletResponse response) {
        System.out.println("쿠키 생성");
        Cookie cookie = new Cookie("useremail","blueskii");
        cookie.setDomain("localhost");
        cookie.setPath("/");
        // 30초간 저장
        cookie.setMaxAge(30*60);
        cookie.setSecure(true);
        response.addCookie(cookie);

        return "";
    }
}
