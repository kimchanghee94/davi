package com.davi_server.traffic.service;

import com.davi_server.traffic.dto.BusRoutePathDto;
import com.davi_server.traffic.repository.BusRoutePathRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class BusRoutePathService {
    private final BusRoutePathRepository busRoutePathRepository;

    public Map<String, Object> selectList(BusRoutePathDto busRoutePathDto){
        Map<String, Object> result = new HashMap<>();

        try{
            List<BusRoutePathDto> busRoutePathDtos = busRoutePathRepository.selectList(busRoutePathDto);
            
            result.put("success", true);
            result.put("message", "Get Bus Route Path List Success");
            result.put("data", busRoutePathDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Bus Route Path List Fail");
        }

        return result;
    }
}
