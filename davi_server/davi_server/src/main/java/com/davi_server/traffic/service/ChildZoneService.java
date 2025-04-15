package com.davi_server.traffic.service;

import com.davi_server.traffic.dto.ChildZoneDto;
import com.davi_server.traffic.dto.CrosswalkDto;
import com.davi_server.traffic.repository.ChildZoneRepository;
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
public class ChildZoneService {
    private final ChildZoneRepository childZoneRepository;

    public Map<String, Object> selectList(){
        Map<String, Object> result = new HashMap<>();

        try{
            List<ChildZoneDto> childZoneDtos = childZoneRepository.selectList();

            result.put("success", true);
            result.put("message", "Get Child Zone List Success");
            result.put("data", childZoneDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Child Zone List Fail");
        }

        return result;
    }

    public Map<String, Object> selectSampleList(){
        Map<String, Object> result = new HashMap<>();

        try{
            List<ChildZoneDto> childZoneDtos = childZoneRepository.selectSampleList();

            result.put("success", true);
            result.put("message", "Get Child Zone Sample List Success");
            result.put("data", childZoneDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Child Zone Sample List Fail");
        }

        return result;
    }
}
