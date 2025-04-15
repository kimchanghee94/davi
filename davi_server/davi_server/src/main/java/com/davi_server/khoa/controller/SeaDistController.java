package com.davi_server.khoa.controller;

import com.davi_server.common.util.ConstantUtil;
import com.davi_server.khoa.service.SeaDistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/khoa/seadist/*")
public class SeaDistController {
    private final SeaDistService seaDistService;

    //http://localhost:7272/khoa/seadist/list
    @RequestMapping(value="/list", method=RequestMethod.POST)
    public Map<String, Object> getList(){
        log.debug("Get Sea Dist List");
        return seaDistService.selectList();
    }

    //http://localhost:7272/khoa/seadist/sample-list
    @RequestMapping(value="/sample-list", method = RequestMethod.POST)
    public Map<String, Object> getSampleList(){
        log.debug("Get Sea Dist Sample List");
        return seaDistService.selectSampleList(ConstantUtil.CACHE_SEADIST_KEY);
    }
}
