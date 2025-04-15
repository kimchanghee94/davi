package com.davi_crawler.disaster.controller;

import com.davi_crawler.disaster.service.ChimsuService;
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
@RequestMapping("/disaster/chimsu/*")
public class ChimsuController {
    private final ChimsuService chimsuService;

    //http://localhost:8282/disaster/chimsu/update
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public Map<String, Object> updateApiData(){
        log.debug("update chimsu api data");
        Map<String, Object> result = new HashMap<>();

        try{
            result = chimsuService.updateApiData();
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "침수 구간 정보 업데이트 실패");
        }

        return result;
    }

}
