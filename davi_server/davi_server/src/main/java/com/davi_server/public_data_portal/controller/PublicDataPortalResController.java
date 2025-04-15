package com.davi_server.public_data_portal.controller;

import com.davi_server.public_data_portal.dto.PublicDataPortalServiceDto;
import com.davi_server.public_data_portal.service.PublicDataPortalResService;
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
@RequestMapping("/public-data-portal/res/*")
public class PublicDataPortalResController {

    private final PublicDataPortalResService publicDataPortalResService;

    //http://localhost:7272/public-data-portal/res/list/public-data-portal-service-id
    @RequestMapping(value="/list/public-data-portal-service-id", method = RequestMethod.POST)
    public Map<String, Object> getListByPublicDataPortalServiceId(@RequestBody PublicDataPortalServiceDto publicDataPortalServiceDto){
        log.debug("Get Public Data Portal Res List By " + publicDataPortalServiceDto);
        return publicDataPortalResService.selectListByPublicDataPortalServiceId(publicDataPortalServiceDto);
    }
}
