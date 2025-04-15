package com.davi_crawler.traffic.controller;

import com.davi_crawler.traffic.service.TrafficLightService;
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
@RequestMapping("/traffic/trafficlight/*")
public class TrafficLightController {
    private final TrafficLightService trafficLightService;

    //http://localhost:8282/traffic/trafficlight/update
    @RequestMapping(value="/update", method= RequestMethod.POST)
    public Map<String, Object> updateApiData(){
        log.debug("update traffic light api data");
        Map<String, Object> result = new HashMap<>();

        try{
            result = trafficLightService.updateApiData();
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "신호등 정보 업데이트 실패");
        }

        return result;
    }
}
