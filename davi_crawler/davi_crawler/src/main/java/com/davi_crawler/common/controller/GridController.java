package com.davi_crawler.common.controller;

import com.davi_crawler.common.service.GridService;
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
@RequestMapping("/common/grid/*")
public class GridController {

    private final GridService gridService;

    //http://localhost:8282/common/grid/update
    @RequestMapping(value="/update", method= RequestMethod.POST)
    public Map<String, Object> updateApiData(){
        log.debug("update grid api data");
        Map<String, Object> result = new HashMap<>();

        try{
            result = gridService.updateApiData();
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "그리드 정보 업데이트 실패");
        }

        return result;
    }
}
