package com.davi_server.common.service;

import com.davi_server.common.dto.DataListDto;
import com.davi_server.common.repository.DataListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class DataListService {
    private final DataListRepository dataListRepository;

    public Map<String, Object> getDataListById(DataListDto dataListDto){
        Map<String, Object> result = new HashMap<>();

        try{
            DataListDto resDataListDto = dataListRepository.selectById(dataListDto);

            result.put("success", true);
            result.put("message", "Get Data List By Id Success");
            result.put("data", resDataListDto);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Data List By Id Fail");
        }

        return result;
    }

    public Map<String, Object> getDataListByMetaType(DataListDto dataListDto){
        Map<String, Object> result = new HashMap<>();

        try{
            List<DataListDto> dataListDtoList = dataListRepository.selectListByMetaType(dataListDto);

            result.put("success", true);
            result.put("message", "Get Data List By Meta Type Success");
            result.put("data", dataListDtoList);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Data List By Meta Type Fail");
        }

        return result;
    }
}
