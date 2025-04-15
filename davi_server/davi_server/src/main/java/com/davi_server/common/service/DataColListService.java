package com.davi_server.common.service;

import com.davi_server.common.dto.DataListDto;
import com.davi_server.common.dto.DataColListDto;
import com.davi_server.common.repository.DataColListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class DataColListService {
    private final DataColListRepository dataColListRepository;

    public Map<String, Object> selectDataColListByDataListId(DataListDto dataListDto){
        Map<String, Object> result = new HashMap<>();

        try{
            List<DataColListDto> dataColListDtos = dataColListRepository.selectByDataListId(dataListDto);

            result.put("success", true);
            result.put("message", "Get Col List By Data List Id Failed");
            result.put("data", dataColListDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Col List By Data List Id Failed");
        }

        return result;
    }
}