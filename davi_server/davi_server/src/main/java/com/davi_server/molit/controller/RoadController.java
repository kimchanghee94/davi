package com.davi_server.molit.controller;

import com.davi_server.common.util.ConstantUtil;
import com.davi_server.molit.service.RoadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/molit/road/*")
public class RoadController {
    private final RoadService roadService;

    //http://localhost:7272/molit/road/list
    @RequestMapping(value="/list", method= RequestMethod.POST)
    public Map<String, Object> getList(){
        log.debug("Get Road List");
        return roadService.selectList();
    }

    //http://localhost:7272/molit/road/sample-list
    @RequestMapping(value="/sample-list", method= RequestMethod.POST)
    public Map<String, Object> getSampleList(){
        log.debug("Get Road Sample List");
        return roadService.selectSampleList(ConstantUtil.CACHE_ROAD_KEY);
    }

}
