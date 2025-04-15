package com.davi_server.traffic.service;

import com.davi_server.traffic.dto.CrosswalkDto;
import com.davi_server.traffic.dto.SpeedDumpDto;
import com.davi_server.traffic.repository.CrosswalkRepository;
import com.davi_server.traffic.repository.SpeedDumpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class SpeedDumpService {
    private final SpeedDumpRepository speedDumpRepository;

    public Map<String, Object> selectList(){
        Map<String, Object> result = new HashMap<>();

        try{
            List<SpeedDumpDto> speedDumpDtos = speedDumpRepository.selectList();

            result.put("success", true);
            result.put("message", "Get Speed Dump List Success");
            result.put("data", speedDumpDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Speed Dump List Fail");
        }

        return result;
    }

    public Map<String, Object> selectSampleList(){
        Map<String, Object> result = new HashMap<>();

        try{
            List<SpeedDumpDto> speedDumpDtos = speedDumpRepository.selectSampleList();

            result.put("success", true);
            result.put("message", "Get Speed Dump Sample List Success");
            result.put("data", speedDumpDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Speed Dump Sample List Fail");
        }

        return result;
    }
}
