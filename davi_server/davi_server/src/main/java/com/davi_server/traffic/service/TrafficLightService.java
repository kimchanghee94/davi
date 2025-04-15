package com.davi_server.traffic.service;

import com.davi_server.traffic.dto.CrosswalkDto;
import com.davi_server.traffic.dto.TrafficLightDto;
import com.davi_server.traffic.repository.CrosswalkRepository;
import com.davi_server.traffic.repository.TrafficLightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class TrafficLightService {
    private final TrafficLightRepository trafficLightRepository;

    public Map<String, Object> selectList(){
        Map<String, Object> result = new HashMap<>();

        try{
            List<TrafficLightDto> trafficLightDtos = trafficLightRepository.selectList();

            result.put("success", true);
            result.put("message", "Get Traffic Light List Success");
            result.put("data", trafficLightDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Traffic Light List Fail");
        }

        return result;
    }

    public Map<String, Object> selectSampleList(){
        Map<String, Object> result = new HashMap<>();

        try{
            List<TrafficLightDto> trafficLightDtos = trafficLightRepository.selectSampleList();

            result.put("success", true);
            result.put("message", "Get Traffic Light Sample List Success");
            result.put("data", trafficLightDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Traffic Light Sample List Fail");
        }

        return result;
    }
}
