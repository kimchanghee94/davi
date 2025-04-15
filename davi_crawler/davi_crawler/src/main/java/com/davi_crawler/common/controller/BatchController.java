package com.davi_crawler.common.controller;

import com.davi_crawler.common.component.MultipleScheduled;
import com.davi_crawler.common.dto.DataListDto;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.util.ApiUtil;
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
@RequestMapping("/common/batch/*")
public class BatchController {
    private final DataListService dataListService;
    private final MultipleScheduled multipleScheduled;


    //http://localhost:8282/common/batch/job
    @RequestMapping(value="/job", method = RequestMethod.POST)
    public Map<String, Object> startJob(@RequestBody Map<String, Object> paramDto) {
        log.debug("Starting the batch Job");
        try{
            String serviceType = (String) paramDto.get("serviceType");
            DataListDto dataListDto = dataListService.selectById(Long.parseLong(serviceType));

            multipleScheduled.setCron(dataListDto.getPeriod());
            return multipleScheduled.startScheduler(serviceType);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //http://localhost:8282/common/batch/job-off
    @RequestMapping(value="/job-off", method = RequestMethod.POST)
    public Map<String, Object> stopJob(@RequestBody Map<String, Object> paramDto){
        String serviceType = (String) paramDto.get("serviceType");

        return multipleScheduled.stopScheduler(serviceType);
    }
}
