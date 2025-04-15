package com.davi_server.traffic.controller;

import com.davi_server.traffic.service.SpeedDumpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/traffic/speeddump/*")
public class SpeedDumpController {
    private final SpeedDumpService speedDumpService;

    //http://localhost:7272/traffic/speeddump/list
    @RequestMapping(value="list", method= RequestMethod.POST)
    public Map<String, Object> getList(){
        log.debug("Get Speed Dump List");
        return speedDumpService.selectList();
    }

    //http://localhost:7272/traffic/speeddump/sample-list
    @RequestMapping(value="sample-list", method=RequestMethod.POST)
    public Map<String, Object> getSampleList(){
        log.debug("Get Speed Dump Sample List");
        return speedDumpService.selectSampleList();
    }
}
