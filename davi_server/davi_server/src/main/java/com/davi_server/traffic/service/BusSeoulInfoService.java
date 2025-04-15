package com.davi_server.traffic.service;

import com.davi_server.traffic.dto.BusSeoulInfoDto;
import com.davi_server.traffic.repository.BusSeoulInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class BusSeoulInfoService {
    private final BusSeoulInfoRepository busSeoulInfoRepository;

    public Map<String, Object> selectList(BusSeoulInfoDto busSeoulInfoDto){
        Map<String, Object> result = new HashMap<>();

        try{
            List<BusSeoulInfoDto> busSeoulInfoDtos = busSeoulInfoRepository.selectList(busSeoulInfoDto);

            result.put("success", true);
            result.put("message", "Get Bus Seoul Info List Success");
            result.put("data", busSeoulInfoDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Bus Seoul Info List Fail");
        }

        return result;
    }

    public Map<String, Object> selectSampleList(BusSeoulInfoDto busSeoulInfoDto){
        Map<String, Object> result = new HashMap<>();

        try{
            List<BusSeoulInfoDto> busSeoulInfoDtos = busSeoulInfoRepository.selectSampleList(busSeoulInfoDto);

            result.put("success", true);
            result.put("message", "Get Bus Seoul Info Sample List Success");
            result.put("data", busSeoulInfoDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Bus Seoul Info Sample List Fail");
        }

        return result;
    }
}
