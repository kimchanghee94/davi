package com.davi_crawler.molit.controller;

import com.davi_crawler.molit.service.RoadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/molit/road/*")
public class RoadController {
    private final RoadService roadService;

    //http://localhost:8282/molit/road/update
    @RequestMapping(value="/update", method= RequestMethod.POST)
    public Map<String, Object> updateApiData(){
        log.debug("update road api data");
        Map<String, Object> result = new HashMap<>();

        try{
            result = roadService.updateApiData();
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "ROAD 데이터 업데이트 실패");
        }

        return result;
    }
}
