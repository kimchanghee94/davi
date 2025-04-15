package com.example.sveltespringboot.controller;

import com.example.sveltespringboot.config.SecurityUtil;
import com.example.sveltespringboot.dto.TokenDto;
import com.example.sveltespringboot.dto.UserDto;
import com.example.sveltespringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/user-api")
@RequiredArgsConstructor
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    //http://localhost:5052/api/v1/user-api/login
    @PostMapping(value="/login")
    public String login(UserDto userDto, HttpServletResponse response) {
        System.out.println("login start");

        String userId = userDto.getUserId();
        String passwd = userDto.getPasswd();

        System.out.println("userDto ID : " + userId + ", passwd : " + passwd);

        TokenDto tokenDto = userService.login(userId, passwd);
        System.out.println(tokenDto.getAccessToken() +"\n"+tokenDto.getRefreshToken()+"\n"+
                tokenDto.getGrantType());

        response.setHeader("Authorization", tokenDto.getGrantType() + " " + tokenDto.getAccessToken());

        JSONObject root = new JSONObject();
        root.put("grantType", tokenDto.getGrantType());
        root.put("accessToken", tokenDto.getAccessToken());
        root.put("refreshToken", tokenDto.getRefreshToken());

        return root.toJSONString();
    }

    //http://localhost:5052/api/v1/user-api/refresh-token-check
    @PostMapping(value="refresh-token-check")
    public String refreshTokenCheck(TokenDto tokenDto) {
        System.out.println("Refresh Token Check!!!");
        System.out.println(tokenDto.getRefreshToken());
        int result = userService.refreshTokenCheck(tokenDto.getRefreshToken());
        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();

        if(result == 0){
            header.put("statusCode", "00");
            header.put("msg", "refresh token is validate");
        }else{
            header.put("statusCode", "01");
            header.put("msg", "refresh token is invalidate.");
        }
        root.put("header", header);
        System.out.println(root.toString());
        return root.toJSONString();
    }

    //http://localhost:5052/api/v1/user-api/refresh-token
    @PostMapping(value="refresh-token")
    public String refreshToken(TokenDto tokenDto, HttpServletResponse response) {
        System.out.println("REFRESH TOKEN START!!!");
        String id = SecurityUtil.getCurrentMemberId();
        System.out.println("id : " + id);

        System.out.println("Refresh Part Access Token : " + tokenDto.getAccessToken());
        System.out.println("Refresh Part Refresh Token : " + tokenDto.getRefreshToken());

        TokenDto refToken = userService.refreshToken(tokenDto);

        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();

        if (refToken != null) {
            response.setHeader("Authorization", refToken.getGrantType() + " " + refToken.getAccessToken());
            header.put("statusCode", "00");
            header.put("msg", "refresh Token Success");

            body.put("grantType", refToken.getGrantType());
            body.put("accessToken", refToken.getAccessToken());
            body.put("refreshToken", refToken.getRefreshToken());
            root.put("body", body);
        }else{
            header.put("statusCode", "01");
            header.put("msg", "refresh Token Failed");
        }
        root.put("header", header);

        System.out.println(root.toString());
        return root.toJSONString();
    }

    //http://localhost:5052/api/v1/user-api/logout
    @PostMapping(value="/logout")
    public String logout(TokenDto tokenDto) {
        System.out.println("Logout START");
        System.out.println("Logout Token Dto AccessToken :" + tokenDto.getAccessToken());
        int result = userService.logout(tokenDto);

        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();

        if(result == 1){
            header.put("statusCode", "01");
            header.put("msg", "Already Access Token is Expired");
        }else if(result == 0){
            header.put("statusCode", "00");
            header.put("msg", "Logout Success");
        }

        root.put("header", header);
        System.out.println("Response Json String : " + root.toString());
        return root.toJSONString();
    }

    //http://localhost:5052/api/v1/user-api/userid
    @GetMapping(value="/userid")
    public String getUserId(@RequestParam String id){

        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();

        UserDto userDto = userService.getUser(id);

        if(userDto == null){
            header.put("statusCode", "01");
            header.put("msg", "Get ["+id+"] from USERS_TB FAILED.");
        }else{
            header.put("statusCode", "00");
            header.put("msg", "Get ["+id+"] from USERS_TB SUCCESS.");

            body.put("userId", userDto.getUserId());
            body.put("passwd", userDto.getPasswd());
            body.put("phone", userDto.getPhone());
            body.put("name", userDto.getName());
            body.put("lastLoginDate", userDto.getLastLoginDate().toString());
            body.put("roleCode", userDto.getRoleDto().getRoleCode());
            body.put("joinDate", userDto.getJoinDate().toString());
            body.put("passwdChangeDate", userDto.getPasswdChangeDate().toString());

            root.put("body", body);
        }

        root.put("header", header);

        System.out.println(root.toString());
        return root.toJSONString();
    }

    //http://localhost:5052/api/v1/user-api/join
    @PostMapping(value="/join")
    public void joinUser(UserDto userDto, HttpServletResponse response) throws IOException {
        String userId = userDto.getUserId();
        String passwd = userDto.getPasswd();
        String phone = userDto.getPhone();
        String name = userDto.getName();
        int roleCode = 1;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String nowDate = sdf.format(now);

        java.sql.Date lastLoginDate = java.sql.Date.valueOf(sdf.format(now));
        java.sql.Date joinDate = lastLoginDate;
        java.sql.Date passwdChangeDate = lastLoginDate;
        userService.saveUser(userId, passwd, phone, name, lastLoginDate, roleCode, joinDate, passwdChangeDate);
        response.sendRedirect("http://localhost:5170/login");
    }
}
