package com.davi_server.common.controller;

import com.davi_server.common.dto.DataListDto;
import com.davi_server.common.service.DataColListService;
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
@RequestMapping(value="/common/data-col-list/*")
public class DataColListController {
    private final DataColListService dataColListService;

    //http://localhost:7272/common/data-col-list/data-list-id
    @RequestMapping(value="/data-list-id", method= RequestMethod.POST)
    public Map<String, Object> getDataColListByDataListId(@RequestBody DataListDto dataListDto){
        log.debug("Get Data Col List By Data List Id " + dataListDto);
        return dataColListService.selectDataColListByDataListId(dataListDto);
    }
}
