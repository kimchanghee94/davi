package com.davi_server.traffic.service;

import com.davi_server.traffic.dto.CrosswalkDto;
import com.davi_server.traffic.dto.FrostDto;
import com.davi_server.traffic.repository.CrosswalkRepository;
import com.davi_server.traffic.repository.FrostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class FrostService {
    private final FrostRepository frostRepository;

    public Map<String, Object> selectList(){
        Map<String, Object> result = new HashMap<>();

        try{
            List<FrostDto> frostDtos = frostRepository.selectList();

            result.put("success", true);
            result.put("message", "Get Frost List Success");
            result.put("data", frostDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Frost List Fail");
        }

        return result;
    }

    public Map<String, Object> selectSampleList(){
        Map<String, Object> result = new HashMap<>();

        try{
            List<FrostDto> frostDtos = frostRepository.selectSampleList();

            result.put("success", true);
            result.put("message", "Get Frost Sample List Success");
            result.put("data", frostDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Frost Sample List Fail");
        }

        return result;
    }
}
