package com.davi_server.traffic.service;

import com.davi_server.traffic.dto.RseDto;
import com.davi_server.traffic.repository.RseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class RseService {
    private final RseRepository rseRepository;

    public Map<String, Object> selectList(){
        Map<String, Object> result = new HashMap<>();

        try{
            List<RseDto> rseDtos = rseRepository.selectList();

            result.put("success", true);
            result.put("message", "Get Rse List Success");
            result.put("data", rseDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Rse List Fail");
        }

        return result;
    }

    public Map<String, Object> selectSampleList(){
        Map<String, Object> result = new HashMap<>();

        try{
            List<RseDto> rseDtos = rseRepository.selectSampleList();

            result.put("success", true);
            result.put("message", "Get Rse Sample List Success");
            result.put("data", rseDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Rse Sample List Fail");
        }

        return result;
    }
}
