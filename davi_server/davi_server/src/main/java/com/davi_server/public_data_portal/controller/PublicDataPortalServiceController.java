package com.davi_server.public_data_portal.controller;

import com.davi_server.public_data_portal.dto.PublicDataPortalDto;
import com.davi_server.public_data_portal.service.PublicDataPortalServiceService;
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
@RequestMapping("/public-data-portal/service/*")
public class PublicDataPortalServiceController {

    private final PublicDataPortalServiceService publicDataPortalServiceService;

    //http://localhost:7272/public-data-portal/service/list/public-data-portal-id
    @RequestMapping(value="/list/public-data-portal-id", method = RequestMethod.POST)
    public Map<String, Object> getListByPublicDataPortalId(@RequestBody PublicDataPortalDto publicDataPortalDto){
        log.debug("Get Public Data Portal Service List By : " + publicDataPortalDto);
        return publicDataPortalServiceService.selectListByPublicDataPortalId(publicDataPortalDto);
    }
}
