package com.davi_server.khoa.service;

import com.davi_server.common.util.ConstantUtil;
import com.davi_server.khoa.dto.LsmdContDto;
import com.davi_server.khoa.repository.LsmdContRepository;
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
public class LsmdContService {
    private final LsmdContRepository lsmdContRepository;

    public Map<String, Object> selectList(){
        Map<String, Object> result = new HashMap<>();
        try{
            List<LsmdContDto> lsmdContDtos = lsmdContRepository.selectList();

            result.put("success", true);
            result.put("message", "Get Lsmd Cont List Success");
            result.put("data", lsmdContDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Lsmd Cont List Fail");
        }

        return result;
    }

    @Cacheable(cacheNames = ConstantUtil.CACHE_KHOA_VALUE , key = "#cacheKey")
    public Map<String, Object> selectSampleList(String cacheKey){
        Map<String, Object> result =  new HashMap<>();
        try{
            List<LsmdContDto> lsmdContDtos = lsmdContRepository.selectSampleList();

            result.put("success", true);
            result.put("message", "Get Lsmd Cont Sample List Success");
            result.put("data", lsmdContDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Lsmd Cont Sample List Fail");
        }
        return result;
    }

    @CachePut(cacheNames = ConstantUtil.CACHE_KHOA_VALUE, key = "#cacheKey")
    public Map<String, Object> reSelectSampleList(String cacheKey){
        Map<String, Object> result =  new HashMap<>();
        try{
            List<LsmdContDto> lsmdContDtos = lsmdContRepository.selectSampleList();

            result.put("success", true);
            result.put("message", "Get Lsmd Cont Sample List Success");
            result.put("data", lsmdContDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Lsmd Cont Sample List Fail");
        }
        return result;
    }
}
