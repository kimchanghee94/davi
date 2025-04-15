package com.davi_crawler.khoa.controller;

import com.davi_crawler.khoa.service.BuoyService;
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
@RequestMapping("/khoa/buoy/*")
public class BuoyController {
    private final BuoyService buoyService;

    //http://localhost:8282/khoa/buoy/update
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public Map<String, Object> updateApiData(){
        log.debug("update buoy api data");
        Map<String, Object> result = new HashMap<>();

        try{
            result = buoyService.updateApiData();
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "등대표 업데이트 실패");
        }

        return result;
    }
}
