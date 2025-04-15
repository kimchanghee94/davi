package com.davi_server.traffic.controller;

import com.davi_server.traffic.service.CrosswalkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/traffic/crosswalk/*")
public class CrosswalkController {
    private final CrosswalkService crosswalkService;

    //http://localhost:7272/traffic/crosswalk/list
    @RequestMapping(value="list", method= RequestMethod.POST)
    public Map<String, Object> getList(){
        log.debug("Get Crosswalk List");
        return crosswalkService.selectList();
    }

    //http://localhost:7272/traffic/crosswalk/list
    @RequestMapping(value="sample-list", method=RequestMethod.POST)
    public Map<String, Object> getSampleList(){
        log.debug("Get Crosswalk Sample List");
        return crosswalkService.selectSampleList();
    }
}
