package com.davi_crawler.traffic.controller;

import com.davi_crawler.traffic.service.SpeedDumpService;
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
@RequestMapping("/traffic/speeddump/*")
public class SpeedDumpController {
    private final SpeedDumpService speedDumpService;


    //http://localhost:8282/traffic/speeddump/update
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public Map<String, Object> updateApiData(){
        log.debug("update speed dump api data");
        Map<String, Object> result = new HashMap<>();

        try{
            result = speedDumpService.updateApiData();
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "과속방지턱 정보 업데이트 실패");
        }

        return result;
    }
}
