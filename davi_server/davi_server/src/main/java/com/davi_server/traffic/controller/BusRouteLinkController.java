package com.davi_server.traffic.controller;

import com.davi_server.traffic.dto.BusRouteLinkDto;
import com.davi_server.traffic.service.BusRouteLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/traffic/busroutelink/*")
public class BusRouteLinkController {
    private final BusRouteLinkService busRouteLinkService;

    //http://localhost:7272/traffic/busroutelink/list
    @RequestMapping(value="list", method= RequestMethod.POST)
    public Map<String, Object> getList(@RequestBody BusRouteLinkDto busRouteLinkDto){
        log.debug("Get Bus Route Link List");
        return busRouteLinkService.selectList(busRouteLinkDto);
    }

    //http://localhost:7272/traffic/busroutelink/sample-list
    @RequestMapping(value="sample-list", method=RequestMethod.POST)
    public Map<String, Object> getSampleList(@RequestBody BusRouteLinkDto busRouteLinkDto){
        log.debug("Get Bus Route Link Sample List");
        return busRouteLinkService.selectSampleList(busRouteLinkDto);
    }
}
