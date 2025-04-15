package com.davi_crawler.molit.controller;

import com.davi_crawler.molit.service.LinkService;
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
@RequestMapping("/molit/link/*")
public class LinkController {
    private final LinkService linkService;

    //http://localhost:8282/molit/link/update
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public Map<String, Object> updateApiData(){
        log.debug("update link api data");
        Map<String, Object> result = new HashMap<>();

        try{
            result = linkService.updateApiData();
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "LINK 데이터 업데이트 실패");
        }

        return result;
    }
}
