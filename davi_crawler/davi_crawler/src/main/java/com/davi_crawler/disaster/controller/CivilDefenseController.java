package com.davi_crawler.disaster.controller;

import com.davi_crawler.disaster.service.CivilDefenseService;
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
@RequestMapping("/disaster/civildefense/*")
public class CivilDefenseController {
    private final CivilDefenseService civilDefenseService;

    //http://localhost:8282/disaster/civildefense/update
    @RequestMapping(value="/update", method= RequestMethod.POST)
    public Map<String, Object> updateApiData(){
        log.debug("update civil defense api data");
        Map<String, Object> result = new HashMap<>();

        try{
            result = civilDefenseService.updateApiData();
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "민방위 정보 업데이트 실패");
        }

        return result;
    }
}
