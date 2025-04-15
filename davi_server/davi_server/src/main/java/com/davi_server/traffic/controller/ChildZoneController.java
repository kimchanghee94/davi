package com.davi_server.traffic.controller;

import com.davi_server.traffic.service.ChildZoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/traffic/childzone/*")
public class ChildZoneController {
    private final ChildZoneService childZoneService;

    //http://localhost:7272/traffic/childzone/list
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Map<String, Object> getList(){
        log.debug("Get Child Zone List");
        return childZoneService.selectList();
    }

    //http://localhost:7272/traffic/childzone/sample-list
    @RequestMapping(value="sample-list", method=RequestMethod.POST)
    public Map<String, Object> getSampleList(){
        log.debug("Get Child Zone Sample List");
        return childZoneService.selectSampleList();
    }
}
