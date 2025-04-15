package com.davi_server.traffic.controller;

import com.davi_server.traffic.service.TrafficLightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/traffic/trafficlight/*")
public class TrafficLightController {
    private final TrafficLightService trafficLightService;

    //http://localhost:7272/traffic/trafficlight/list
    @RequestMapping(value="list", method= RequestMethod.POST)
    public Map<String, Object> getList(){
        log.debug("Get Traffic Light List");
        return trafficLightService.selectList();
    }

    //http://localhost:7272/traffic/trafficlight/sample-list
    @RequestMapping(value="sample-list", method=RequestMethod.POST)
    public Map<String, Object> getSampleList(){
        log.debug("Get Traffic Light Sample List");
        return trafficLightService.selectSampleList();
    }
}
