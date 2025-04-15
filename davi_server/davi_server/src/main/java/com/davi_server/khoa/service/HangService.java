package com.davi_server.khoa.service;

import com.davi_server.common.util.ConstantUtil;
import com.davi_server.khoa.dto.HangDto;
import com.davi_server.khoa.repository.HangRepository;
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
public class HangService {
    private final HangRepository hangRepository;

    public Map<String, Object> selectList(){
        Map<String, Object> result = new HashMap<>();
        try{
            List<HangDto> hangDtos = hangRepository.selectList();

            result.put("success", true);
            result.put("message", "Get Hang List Success");
            result.put("data", hangDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Hang List Fail");
        }

        return result;
    }

    //선택된 좌표 부근의 데이터만 뽑아온다.
    public Map<String, Object> selectClickPosList(Map<String, Object> paramDto){
        Map<String, Object> result = new HashMap<>();

        try{

            result.put("success", true);
            result.put("message", "Get Hang Click Pos List Success");
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Hang Click Pos List Fail");
        }

        return result;
    }

    @Cacheable(cacheNames = ConstantUtil.CACHE_KHOA_VALUE , key = "#cacheKey")
    public Map<String, Object> selectSampleList(String cacheKey){
        Map<String, Object> result =  new HashMap<>();
        try{
            List<HangDto> hangDtos = hangRepository.selectSampleList();

            result.put("success", true);
            result.put("message", "Get Hang Sample List Success");
            result.put("data", hangDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Hang Sample List Fail");
        }
        return result;
    }

    @CachePut(cacheNames = ConstantUtil.CACHE_KHOA_VALUE, key = "#cacheKey")
    public Map<String, Object> reSelectSampleList(String cacheKey){
        Map<String, Object> result =  new HashMap<>();
        try{
            List<HangDto> hangDtos = hangRepository.selectSampleList();

            result.put("success", true);
            result.put("message", "Get Hang Sample List Success");
            result.put("data", hangDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Hang Sample List Fail");
        }
        return result;
    }
}
