package com.davi_crawler.disaster.controller;

import com.davi_crawler.disaster.service.Seoul113Service;
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
@RequestMapping("/disaster/seoul113/*")
public class Seoul113Controller {
    private final Seoul113Service seoul113Service;

    //http://localhost:8282/disaster/seoul113/update
    @RequestMapping(value="/update", method= RequestMethod.POST)
    public Map<String, Object> updateApiData(){
        log.debug("update seoul 113 api data");
        Map<String, Object> result = new HashMap<>();

        try{
            result = seoul113Service.updateApiData();
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "서울 113 정보 업데이트 실패");
        }

        return result;
    }

}
