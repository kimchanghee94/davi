package com.davi_server.traffic.controller;

import com.davi_server.traffic.service.RseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/traffic/rse/*")
public class RseController {
    private final RseService rseService;

    //http://localhost:7272/traffic/rse/list
    @RequestMapping(value="list", method = RequestMethod.POST)
    public Map<String, Object> getList(){
        log.debug("Get Rse List");
        return rseService.selectList();
    }

    //http://localhost:7272/traffic/rse/sample-list
    @RequestMapping(value="sample-list", method=RequestMethod.POST)
    public Map<String, Object> getSampleList(){
        log.debug("Get Rse Sample List");
        return rseService.selectSampleList();
    }
}
