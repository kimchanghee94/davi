package com.davi_server.molit.service;

import com.davi_server.common.util.ConstantUtil;
import com.davi_server.molit.dto.LiDto;
import com.davi_server.molit.repository.LiRepository;
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
public class LiService {
    private final LiRepository liRepository;

    public Map<String, Object> selectList(){
        Map<String, Object> result = new HashMap<>();
        try{
            List<LiDto> liDtos = liRepository.selectList();

            result.put("success", true);
            result.put("message", "Get Li List Success");
            result.put("data", liDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Li List Fail");
        }

        return result;
    }

    @Cacheable(cacheNames = ConstantUtil.CACHE_MOLIT_VALUE, key = "#cacheKey")
    public Map<String, Object> selectSampleList(String cacheKey){
        Map<String, Object> result = new HashMap<>();
        try{
            List<LiDto> liDtos = liRepository.selectSampleList();

            result.put("success", true);
            result.put("message", "Get Li Sample List Success");
            result.put("data", liDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Li Sample List Fail");
        }

        return result;
    }

    @CachePut(cacheNames = ConstantUtil.CACHE_MOLIT_VALUE, key = "#cacheKey")
    public Map<String, Object> reSelectSampleList(String cacheKey){
        Map<String, Object> result = new HashMap<>();
        try{
            List<LiDto> liDtos = liRepository.selectSampleList();

            result.put("success", true);
            result.put("message", "Get Li Sample List Success");
            result.put("data", liDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Li Sample List Fail");
        }

        return result;
    }
}
