package com.davi_server.khoa.controller;

import com.davi_server.common.util.ConstantUtil;
import com.davi_server.khoa.service.BuoyService;
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
@RequestMapping("/khoa/buoy/*")
public class BuoyController {
    private final BuoyService buoyService;

    //http://localhost:7272/khoa/buoy/list
    @RequestMapping(value="/list", method= RequestMethod.POST)
    public Map<String, Object> getList(){
        log.debug("Get Buoy List");
        return buoyService.selectList();
    }

    //http://localhost:7272/khoa/buoy/sample-list
    @RequestMapping(value="/sample-list", method = RequestMethod.POST)
    public Map<String, Object> getSampleList(){
        log.debug("Get Buoy Sample List");
        return buoyService.selectSampleList(ConstantUtil.CACHE_BUOY_KEY);
    }

    //http://localhost:7272/khoa/buoy/click-pos-list
    @RequestMapping(value="/click-pos-list", method = RequestMethod.POST)
    public Map<String, Object> getClickPosList(@RequestBody Map<String, Object> paramDto){
        log.debug("Get Buoy Click Pos List");
        return buoyService.selectClickPosList(paramDto);
    }
}
