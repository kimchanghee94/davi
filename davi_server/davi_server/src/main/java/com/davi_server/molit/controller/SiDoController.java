package com.davi_server.molit.controller;

import com.davi_server.common.util.ConstantUtil;
import com.davi_server.molit.service.SiDoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/molit/sido/*")
public class SiDoController {
    private final SiDoService siDoService;

    //http://localhost:7272/molit/sido/list
    @RequestMapping(value="/list", method= RequestMethod.POST)
    public Map<String, Object> getList(){
        log.debug("Get SiDo List");
        return siDoService.selectList();
    }

    //http://localhost:7272/molit/sido/sample-list
    @RequestMapping(value="/sample-list", method= RequestMethod.POST)
    public Map<String, Object> getSampleList(){
        log.debug("Get SiDo Sample List");
        return siDoService.selectSampleList(ConstantUtil.CACHE_SIDO_KEY);
    }

}
