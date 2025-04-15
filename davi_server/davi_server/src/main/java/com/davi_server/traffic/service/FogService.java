package com.davi_server.traffic.service;

import com.davi_server.traffic.dto.CrosswalkDto;
import com.davi_server.traffic.dto.FogDto;
import com.davi_server.traffic.repository.CrosswalkRepository;
import com.davi_server.traffic.repository.FogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class FogService {
    private final FogRepository fogRepository;

    public Map<String, Object> selectList(){
        Map<String, Object> result = new HashMap<>();

        try{
            List<FogDto> fogDtos = fogRepository.selectList();

            result.put("success", true);
            result.put("message", "Get Fog List Success");
            result.put("data", fogDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Fog List Fail");
        }

        return result;
    }

    public Map<String, Object> selectSampleList(){
        Map<String, Object> result = new HashMap<>();

        try{
            List<FogDto> fogDtos = fogRepository.selectSampleList();

            result.put("success", true);
            result.put("message", "Get Fog Sample List Success");
            result.put("data", fogDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Fog Sample List Fail");
        }

        return result;
    }
}
