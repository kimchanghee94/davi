package com.davi_crawler.molit.controller;

import com.davi_crawler.molit.service.EupMyeonDongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/molit/eupmyeondong/*")
public class EupMyeonDongController {
    private final EupMyeonDongService eupMyeonDongService;

    //http://localhost:8282/molit/eupmyeondong/update
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public Map<String, Object> updateApiData(){
        log.debug("update eup myeon dong api data");
        Map<String, Object> result = new HashMap<>();

        try {
            result = eupMyeonDongService.updateApiData();
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "EUPMYEONDONG 데이터 업데이트 실패");
        }

        return result;
    }

    //http://localhost:8282/molit/eupmyeondong/update-hdong-bdong
    @RequestMapping(value="/update/hdong-bdong", method = RequestMethod.POST)
    public Map<String, Object> updateApiDataHdongBdong(){
        log.debug("update eup myeon dong hdong, bdong api data");
        Map<String, Object> result = new HashMap<>();

        try{
            result = eupMyeonDongService.updateHdongBdongApiData();
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "hdong, bdong 데이터 업데이트 실패");
        }

        return result;
    }
}
