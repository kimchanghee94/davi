package com.davi_server.public_data_portal.controller;

import com.davi_server.public_data_portal.service.PublicDataPortalService;
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

    //http://localhost:7272/public-data-portal/list/table-json-flag
    @RequestMapping(value="list/table-json-flag", method= RequestMethod.POST)
    public Map<String, Object> getPublicDataPortalListByTableJsonFlag(@RequestBody Map<String, Object> paramDto){
       log.debug("Get Public Data Portal List By " + paramDto);
       return publicDataPortalService.selectListByTableJsonFlag();
    }
}
