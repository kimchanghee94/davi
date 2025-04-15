package com.davi_server.molit.controller;

import com.davi_server.common.util.ConstantUtil;
import com.davi_server.molit.dto.EupMyeonDongDto;
import com.davi_server.molit.service.EupMyeonDongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/molit/eupmyeondong/*")
public class EupMyeonDongController {
    private final EupMyeonDongService eupMyeonDongService;

    //http://localhost:7272/molit/eupmyeondong/list/id
    @RequestMapping(value="/list/id", method=RequestMethod.POST)
    public Map<String, Object> getListById(@RequestBody List<EupMyeonDongDto> eupMyeonDongDtos){
        log.debug("Get EupMyoenDong List By Id " + eupMyeonDongDtos);
        return null;
    }

    //http://localhost:7272/molit/eupmyeondong/list
    @RequestMapping(value="/list", method=RequestMethod.POST)
    public Map<String, Object> getList(@RequestBody EupMyeonDongDto eupMyeonDongDto){
        log.debug("Get EupMyeonDong List");
        return eupMyeonDongService.selectList(eupMyeonDongDto);
    }

    //http://localhost:7272/molit/eupmyeondong/sample-list
    @RequestMapping(value="/sample-list", method=RequestMethod.POST)
    public Map<String, Object> getSampleList(){
        log.debug("Get EupMyeonDong Sample List");
        return eupMyeonDongService.selectSampleList(ConstantUtil.CACHE_EUPMYEONDONG_KEY);
    }
}
