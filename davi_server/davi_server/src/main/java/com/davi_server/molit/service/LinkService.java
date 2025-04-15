package com.davi_server.molit.service;

import com.davi_server.common.util.ConstantUtil;
import com.davi_server.molit.dto.LinkDto;
import com.davi_server.molit.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;

    public Map<String, Object> selectByPropLinkId(List<LinkDto> linkDtos){
        Map<String, Object> result = new HashMap<>();
        try{
            List<LinkDto> tmplinkDtos = new ArrayList<>();
            for(int i=0; i<linkDtos.size(); i++){
                LinkDto tmpLinkDto = linkRepository.selectByPropLinkId(linkDtos.get(i));
                if(tmpLinkDto != null && linkDtos.get(i).getParamSpeed() != null){
                    tmpLinkDto.setParamSpeed(linkDtos.get(i).getParamSpeed());

                    int maxSpeed = Integer.parseInt(tmpLinkDto.getMaxSpd());
                    int speed = Integer.parseInt(linkDtos.get(i).getParamSpeed());

                    double rem = (double)speed / maxSpeed;

                    if(rem >= 0.8){
                        tmpLinkDto.setParamTrfCgt("3");
                    }else if(rem >= 0.6){
                        tmpLinkDto.setParamTrfCgt("2");
                    }else if(rem >= 0.4){
                        tmpLinkDto.setParamTrfCgt("1");
                    }else {
                        tmpLinkDto.setParamTrfCgt("0");
                    }
                    tmplinkDtos.add(tmpLinkDto);
                }else if(tmpLinkDto != null){
                    tmplinkDtos.add(tmpLinkDto);
                }

            }

            result.put("success", true);
            result.put("message", "Get Link List Success");
            result.put("data", tmplinkDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Link List Fail");
        }
        log.debug("Result Get LinkDtos : " + result);

        return result;
    }

    public Map<String, Object> selectList(){
        Map<String, Object> result = new HashMap<>();
        try{
            List<LinkDto> linkDtos = linkRepository.selectList();

            result.put("success", true);
            result.put("message", "Get Link Sample List Success");
            result.put("data", linkDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Link Sample List Fail");
        }

        return result;
    }

    @Cacheable(cacheNames = ConstantUtil.CACHE_MOLIT_VALUE, key = "#cacheKey")
    public Map<String, Object> selectSampleList(String cacheKey){
        Map<String, Object> result = new HashMap<>();
        try{
            List<LinkDto> linkDtos = linkRepository.selectSampleList();

            result.put("success", true);
            result.put("message", "Get Link Sample List Success");
            result.put("data", linkDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Link Sample List Fail");
        }

        return result;
    }

    @CachePut(cacheNames = ConstantUtil.CACHE_MOLIT_VALUE, key = "#cacheKey")
    public Map<String, Object> reSelectSampleList(String cacheKey){
        Map<String, Object> result = new HashMap<>();
        try{
            List<LinkDto> linkDtos = linkRepository.selectSampleList();

            result.put("success", true);
            result.put("message", "Get Link Info Success");
            result.put("data", linkDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Link Info Fail");
        }

        return result;
    }
}
