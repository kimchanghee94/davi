package com.davi_server.traffic.controller;

import com.davi_server.traffic.service.FogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/traffic/fog/*")
public class FogController {
    private final FogService fogService;

    //http://localhost:7272/traffic/fog/list
    @RequestMapping(value="list", method= RequestMethod.POST)
    public Map<String, Object> getList(){
       log.debug("Get Fog List");
       return fogService.selectList();
    }

    //http://localhost:7272/traffic/fog/sample-list
    @RequestMapping(value="sample-list", method=RequestMethod.POST)
    public Map<String,Object> getSampleList(){
        log.debug("Get For Sample List");
        return fogService.selectSampleList();
    }
}
