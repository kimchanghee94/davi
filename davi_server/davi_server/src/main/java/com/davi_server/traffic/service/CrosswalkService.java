package com.davi_server.traffic.service;

import com.davi_server.traffic.dto.CrosswalkDto;
import com.davi_server.traffic.repository.CrosswalkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class CrosswalkService {
    private final CrosswalkRepository crosswalkRepository;

    public Map<String, Object> selectList(){
        Map<String, Object> result = new HashMap<>();

        try{
            List<CrosswalkDto> crossWalkDtos = crosswalkRepository.selectList();

            result.put("success", true);
            result.put("message", "Get Crosswalk List Success");
            result.put("data", crossWalkDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Crosswalk List Fail");
        }

        return result;
    }

    public Map<String, Object> selectSampleList(){
        Map<String, Object> result = new HashMap<>();

        try{
            List<CrosswalkDto> crosswalkDtos = crosswalkRepository.selectSampleList();

            result.put("success", true);
            result.put("message", "Get Crosswalk Sample List Success");
            result.put("data", crosswalkDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Crosswalk Sample List Fail");
        }

        return result;
    }
}
