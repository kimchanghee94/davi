package com.davi_crawler.public_data_portal.controller;

import com.davi_crawler.public_data_portal.dto.PublicDataPortalDto;
import com.davi_crawler.public_data_portal.dto.PublicDataPortalServiceDto;
import com.davi_crawler.public_data_portal.service.PublicDataPortalService;
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
@RequestMapping("/public-data-portal/*")
public class PublicDataPortalController {
    private final PublicDataPortalService publicDataPortalService;

    //http://localhost:8282/public-data-portal/list
    @RequestMapping(value="/api-list", method = RequestMethod.POST)
    public Map<String, Object> apiList(@RequestBody Map<String, Object> paramDto){
        log.debug("get public-data-portal list : " + paramDto);
        return publicDataPortalService.getPublicDataPortalList((Integer)paramDto.get("listCnt"));
    }

    //http://localhost:8282/public-data-portal/service-list
    @RequestMapping(value="/service-list", method = RequestMethod.POST)
    public Map<String, Object> serviceList(@RequestBody PublicDataPortalDto publicDataPortalDto){
        log.debug("get public-data-portal service list : " + publicDataPortalDto);
        return publicDataPortalService.getPublicDataPortalServiceList(publicDataPortalDto);
    }

    //http://localhost:8282/public-data-portal/req-res-list
    @RequestMapping(value="/req-res-list", method = RequestMethod.POST)
    public Map<String, Object> reqResList(@RequestBody PublicDataPortalServiceDto publicDataPortalServiceDto){
        log.debug("get public-data-portal req res list : " + publicDataPortalServiceDto);
        return publicDataPortalService.getPublicDataPortalReqResList(publicDataPortalServiceDto);
    }
}
