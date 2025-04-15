package com.davi_server.molit.service;

import com.davi_server.common.util.ConstantUtil;
import com.davi_server.molit.dto.RoadDto;
import com.davi_server.molit.repository.RoadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class RoadService {
    private final RoadRepository roadRepository;

    public Map<String, Object> selectList(){
        Map<String, Object> result = new HashMap<>();
        try{
            List<RoadDto> roadDtos = roadRepository.selectList();

            result.put("success", true);
            result.put("message", "Get Road List Success");
            result.put("data", roadDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Road List Fail");
        }

        return result;
    }

    @Cacheable(cacheNames = ConstantUtil.CACHE_MOLIT_VALUE, key = "#cacheKey")
    public Map<String, Object> selectSampleList(String cacheKey){
        Map<String, Object> result = new HashMap<>();
        try{
            List<RoadDto> roadDtos = roadRepository.selectSampleList();

            result.put("success", true);
            result.put("message", "Get Road Sample List Success");
            result.put("data", roadDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Road Sample List Fail");
        }

        return result;
    }

    @CachePut(cacheNames = ConstantUtil.CACHE_MOLIT_VALUE, key = "#cacheKey")
    public Map<String, Object> reSelectSampleList(String cacheKey){
        Map<String, Object> result = new HashMap<>();
        try{
            List<RoadDto> roadDtos = roadRepository.selectSampleList();

            result.put("success", true);
            result.put("message", "Get Road Sample List Success");
            result.put("data", roadDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Road Sample List Fail");
        }

        return result;
    }
}