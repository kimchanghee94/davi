package com.davi_server.traffic.service;

import com.davi_server.molit.dto.LinkDto;
import com.davi_server.molit.service.LinkService;
import com.davi_server.traffic.dto.BusRouteLinkDto;
import com.davi_server.traffic.repository.BusRouteLinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class BusRouteLinkService {
    private final BusRouteLinkRepository busRouteLinkRepository;
    private final LinkService linkService;

    public Map<String, Object> selectList(BusRouteLinkDto busRouteLinkDto){
        Map<String, Object> result = new HashMap<>();

        try{
            List<BusRouteLinkDto> busRouteLinkDtos = busRouteLinkRepository.selectList(busRouteLinkDto);

            List<LinkDto> linkDtos = new ArrayList<>();
            for(BusRouteLinkDto tmpBusRouteLinkDto : busRouteLinkDtos){
                LinkDto linkDto = new LinkDto();

                linkDto.setPropLinkId(tmpBusRouteLinkDto.getLinkId());
                linkDtos.add(linkDto);
            }

            Map<String, Object> linkMap = linkService.selectByPropLinkId(linkDtos);

            linkDtos = (List<LinkDto>) linkMap.get("data");

            //>>>버스 노선중에 링크 값이 null인 경우는 제외시킨다.
            /*if(linkDtos.contains(null) == true){
                result.put("success", false);
                result.put("message", "Get Bus Route Link Success");
                return result;
            }*/
            //<<<버스 노선중에 링크 값이 null인 경우는 제외시킨다.

            result.put("success", true);
            result.put("message", "Get Bus Route Link List Success");
            result.put("data", linkDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Bus Route Link List Fail");
        }

        return result;
    }

    public Map<String, Object> selectSampleList(BusRouteLinkDto busRouteLinkDto){
        Map<String, Object> result = new HashMap<>();

        try{
            List<BusRouteLinkDto> busRouteLinkDtos = busRouteLinkRepository.selectSampleList(busRouteLinkDto);

            result.put("success", true);
            result.put("message", "Get Bus Route Link Sample List Success");
            result.put("data", busRouteLinkDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Bus Route Link Sample List Fail");
        }

        return result;
    }
}