package com.davi_crawler.khoa.controller;

import com.davi_crawler.khoa.service.SeaDistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/khoa/seadist/*")
public class SeaDistController {
    private final SeaDistService seaDistService;

    //http://localhost:8282/khoa/seadist/update
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public Map<String, Object> updateApiData(){
        log.debug("update sea dist api data");
        Map<String, Object> result = new HashMap<>();

        try{
            result = seaDistService.updateApiData();
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "해양 거리 업데이트 실패");
        }

        return result;
    }
}