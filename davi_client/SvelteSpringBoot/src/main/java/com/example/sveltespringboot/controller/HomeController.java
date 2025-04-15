package com.example.sveltespringboot.controller;

import com.example.sveltespringboot.dto.UserSessionDto;
import com.example.sveltespringboot.entity.NotiEntity;
import com.example.sveltespringboot.service.NotiService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class HomeController {

    @Autowired
    private NotiService notiService;

    @GetMapping("dbtest") //localhost:5052/dbtest
    public String boardWriteForm(HttpServletRequest request){
        System.out.println("dbtest start");

        HttpSession session = request.getSession();
        if(session == null){
            System.out.println("Session is null");
        }else{
            System.out.println("Session id " + session.getId());
            System.out.println("Session is exist");
            UserSessionDto usd = (UserSessionDto) session.getAttribute("user");

            if(usd != null) {
                System.out.println("Session GET " + usd.getName());
                System.out.println("Session GET " + usd.getRoleDto().getRoleName());
            }
        }

        //test
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            System.out.println("dbtest: "+auth.getPrincipal());
        }
        List<NotiEntity> notiEntityList = notiService.getListNoti();

        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        JSONArray items = new JSONArray();

        for(NotiEntity notiEntity : notiEntityList){
            System.out.println(notiEntity);

            JSONObject temp = new JSONObject();
            temp.put("id", notiEntity.getId());
            temp.put("title", notiEntity.getTitle());

            items.add(temp);
        }
        body.put("items", items);

        header.put("statusCode", "00");
        header.put("msg", "get Noti Data Success");

        root.put("header", header);
        root.put("body", body);

        System.out.println(root.toString());

        return root.toJSONString();
    }

    @GetMapping("hello")
    public String hello() {
        //json
        return new String("{\"a\":\"Back단에서 호출된 문자열입니다.\"}");
    }
}
