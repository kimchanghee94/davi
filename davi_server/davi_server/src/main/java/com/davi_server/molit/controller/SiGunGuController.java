package com.davi_server.molit.controller;

import com.davi_server.common.util.ConstantUtil;
import com.davi_server.molit.dto.SiGunGuDto;
import com.davi_server.molit.service.SiGunGuService;
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
@RequestMapping("/molit/sigungu/*")
public class SiGunGuController {
    private final SiGunGuService siGunGuService;

    //http://localhost:7272/molit/sigungu/list
    @RequestMapping(value="/list", method = RequestMethod.POST)
    public Map<String, Object> getList(@RequestBody SiGunGuDto siGunGuDto){
        log.debug("Get SiGunGu List");
        return siGunGuService.selectList(siGunGuDto);
    }

    //http://localhost:7272/molit/sigungu/sample-list
    @RequestMapping(value="/sample-list", method = RequestMethod.POST)
    public Map<String, Object> getSampleList(){
        log.debug("Get SiGunGu Sample List");
        return siGunGuService.selectSampleList(ConstantUtil.CACHE_SIGUNGU_KEY);
    }
}
