package com.davi_server.traffic.controller;

import com.davi_server.traffic.dto.BusSeoulInfoDto;
import com.davi_server.traffic.service.BusSeoulInfoService;
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
@RequestMapping("/traffic/busseoulinfo/*")
public class BusSeoulInfoController {
    private final BusSeoulInfoService busSeoulInfoService;

    //http://localhost:7272/traffic/busseoulinfo/list
    @RequestMapping(value="list", method = RequestMethod.POST)
    public Map<String, Object> getList(@RequestBody BusSeoulInfoDto busSeoulInfoDto){
        log.debug("Get Bus Seoul Info List");
        return busSeoulInfoService.selectList(busSeoulInfoDto);
    }

    //http://localhost:7272/traffic/busseoulinfo/sample-list
    @RequestMapping(value="sample-list", method = RequestMethod.POST)
    public Map<String, Object> getSampleList(@RequestBody BusSeoulInfoDto busSeoulInfoDto){
        log.debug("Get Bus Seoul Info Sample List");
        return busSeoulInfoService.selectSampleList(busSeoulInfoDto);
    }
}
