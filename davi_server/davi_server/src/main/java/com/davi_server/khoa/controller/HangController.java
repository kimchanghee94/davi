package com.davi_server.khoa.controller;

import com.davi_server.common.util.ConstantUtil;
import com.davi_server.khoa.service.HangService;
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
@RequestMapping("/khoa/hang/*")
public class HangController {
    private final HangService hangService;

    //http://localhost:7272/khoa/hang/list
    @RequestMapping(value="/list", method=RequestMethod.POST)
    public Map<String, Object> getList(){
        log.debug("Get Hang List");
        return hangService.selectList();
    }

    //http://localhost:7272/khoa/hang/sample-list
    @RequestMapping(value="sample-list", method=RequestMethod.POST)
    public Map<String, Object> getSampleList(){
        log.debug("Get Hang Sample List");
        return hangService.selectSampleList(ConstantUtil.CACHE_HANG_KEY);
    }

    //http://localhost:7272/khoa/hang/click-pos-list
    @RequestMapping(value="/click-pos-list", method = RequestMethod.POST)
    public Map<String, Object> getClickPosList(@RequestBody Map<String, Object> paramDto){
        log.debug("Get Hang Click Pos List");
        return hangService.selectClickPosList(paramDto);
    }
}
