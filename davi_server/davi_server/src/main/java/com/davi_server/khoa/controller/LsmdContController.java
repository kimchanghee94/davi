package com.davi_server.khoa.controller;

import com.davi_server.common.util.ConstantUtil;
import com.davi_server.khoa.service.LsmdContService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/khoa/lsmdcont/*")
public class LsmdContController {
    private final LsmdContService lsmdContService;

    //http://localhost:7272/khoa/lsmdcont/list
    @RequestMapping(value="/list", method = RequestMethod.POST)
    public Map<String, Object> getList(){
        log.debug("Get Lsmd Cont List");
        return lsmdContService.selectList();
    }

    //http://localhost:7272/khoa/lsmdcont/sample-list
    @RequestMapping(value="/sample-list", method=RequestMethod.POST)
    public Map<String, Object> getSampleList(){
        log.debug("Get Lsmd Cont Sample List");
        return lsmdContService.selectSampleList(ConstantUtil.CACHE_LSMDCONT_KEY);
    }
}
