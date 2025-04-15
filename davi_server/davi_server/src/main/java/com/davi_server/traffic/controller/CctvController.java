package com.davi_server.traffic.controller;

import com.davi_server.traffic.service.CctvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/traffic/cctv/*")
public class CctvController {
    private final CctvService cctvService;

    //http://localhost:7272/traffic/cctv/list
    @RequestMapping(value="list", method = RequestMethod.POST)
    public Map<String, Object> getList(){
        log.debug("Get Cctv List");
        return cctvService.selectList();
    }

    //http://localhost:7272/traffic/cctv/sample-list
    @RequestMapping(value="sample-list", method = RequestMethod.POST)
    public Map<String, Object> getSampleList(){
        log.debug("Get Cctv Sample List");
        return cctvService.selectSampleList();
    }
}
