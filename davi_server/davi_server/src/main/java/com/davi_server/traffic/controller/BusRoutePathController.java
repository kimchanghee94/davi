package com.davi_server.traffic.controller;

import com.davi_server.traffic.dto.BusRoutePathDto;
import com.davi_server.traffic.service.BusRoutePathService;
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
@RequestMapping("/traffic/busroutepath/*")
public class BusRoutePathController {
    private final BusRoutePathService busRoutePathService;

    //http://localhost:7272/traffic/busroutepath/list
    @RequestMapping(value="list", method = RequestMethod.POST)
    public Map<String, Object> getList(@RequestBody BusRoutePathDto busRoutePathDto){
        log.debug("Get Bus Route Path List " + busRoutePathDto);
        return busRoutePathService.selectList(busRoutePathDto);
    }
}
