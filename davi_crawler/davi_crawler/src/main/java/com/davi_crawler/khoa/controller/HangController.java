package com.davi_crawler.khoa.controller;

import com.davi_crawler.khoa.service.HangService;
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
@RequestMapping("/khoa/hang/*")
public class HangController {
    private final HangService hangService;

    //http://localhost:8282/khoa/hang/update
    @RequestMapping(value="/update", method= RequestMethod.POST)
    public Map<String, Object> updateApiData(){
        log.debug("update hang api data");
        Map<String, Object> result = new HashMap<>();

        try{
            result = hangService.updateApiData();
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "항 업데이트 실패");
        }

        return result;
    }
}
