package com.davi_server.common.controller;

import com.davi_server.common.dto.DataListDto;
import com.davi_server.common.service.DataListService;
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
@RequestMapping(value="/common/data-list/*")
public class DataListController {
    private final DataListService dataListService;

    //http://localhost:7272/common/data-list/meta-type
    @RequestMapping(value="/meta-type", method = RequestMethod.POST)
    public Map<String, Object> getDataListByMetaType(@RequestBody DataListDto dataListDto){
        log.debug("Get Data List By Meta Type " + dataListDto);
        return dataListService.getDataListByMetaType(dataListDto);
    }
}
