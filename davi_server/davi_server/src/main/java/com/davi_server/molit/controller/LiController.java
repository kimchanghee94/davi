package com.davi_server.molit.controller;

import com.davi_server.common.util.ConstantUtil;
import com.davi_server.molit.service.LiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/molit/li/*")
public class LiController {
    private final LiService liService;

    //http://localhost:7272/molit/li/list
    @RequestMapping(value="/list", method= RequestMethod.POST)
    public Map<String, Object> getList(){
        log.debug("Get Li List");
        return liService.selectList();
    }

    //http://localhost:7272/molit/li/sample-list
    @RequestMapping(value="/sample-list", method= RequestMethod.POST)
    public Map<String, Object> getSampleList(){
        log.debug("Get Li Sample List");
        return liService.selectSampleList(ConstantUtil.CACHE_LI_KEY);
    }
}
