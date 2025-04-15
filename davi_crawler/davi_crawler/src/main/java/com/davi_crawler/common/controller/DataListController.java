package com.davi_crawler.common.controller;

import com.davi_crawler.common.dto.DataListDto;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.util.ConstantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/common/*")
public class DataListController {
    private final DataListService dataListService;

    //http://localhost:8282/common/insert/data-list-info
    @RequestMapping(value = "/insert/data-list-info", method = RequestMethod.POST)
    public void insertDataList(DataListDto dataListDto){
        log.debug("Insert data-list-info");

        try{
            dataListService.insert(dataListDto);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //http://localhost:8282/common/select/data-list-info
    @RequestMapping(value = "/select/data-list-info", method = RequestMethod.GET)
    public List<DataListDto> selectDataList(){
        log.debug("Select data-list-info");
        List<DataListDto> dataListDtoList = null;

        try{
            dataListDtoList = dataListService.selectAll(ConstantUtil.DATA_LIST_INIT_CACHE_KEY);
        }catch (Exception e){
            e.printStackTrace();
        }

        return dataListDtoList;
    }

    //http://localhost:8282/common/re-select/data-list-info
    @RequestMapping(value = "/re-select/data-list-info", method = RequestMethod.GET)
    public List<DataListDto> reSelectDataList(){
        log.debug("Reselect data-list-info");
        List<DataListDto> dataListDtoList = null;

        try{
            dataListDtoList = dataListService.reSelectAll(ConstantUtil.DATA_LIST_INIT_CACHE_KEY);
        }catch (Exception e){
            e.printStackTrace();
        }

        return dataListDtoList;
    }
}
