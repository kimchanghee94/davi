package com.davi_server.common.service;

import com.davi_server.common.dto.DataListDto;
import com.davi_server.common.dto.JoinDto;
import com.davi_server.common.repository.DataListRepository;
import com.davi_server.common.util.ConstantUtil;
import com.davi_server.molit.dto.*;
import com.davi_server.common.repository.JoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class JoinService {
    private final JoinRepository joinRepository;
    private final DataListRepository dataListRepository;

    public Map<String, Object> selectRelatedList(JoinDto joinDto){
        Map<String, Object> result = new HashMap<>();
        try{
            //테이블 명을 찾는다.
            List<DataListDto> dataListDtos = dataListRepository.selectList();
            for(int i=0; i<dataListDtos.size(); i++){
                DataListDto dataListDto = dataListDtos.get(i);
                if(dataListDto.getName().equals(joinDto.getRouteName())){

                    if(dataListDto.getDataListId() == ConstantUtil.DATA_LIST_LINK_ID){
                        List<LinkDto> linkDtos = joinRepository.selectLinkRelatedList(joinDto);
                        result.put("data", linkDtos);
                    }else if(dataListDto.getDataListId() == ConstantUtil.DATA_LIST_SIDO_ID){
                        List<SiDoDto> siDoDtos = joinRepository.selectSiDoRelatedList(joinDto);
                        result.put("data", siDoDtos);
                    }else if(dataListDto.getDataListId() == ConstantUtil.DATA_LIST_SIGUNGU_ID){
                        List<SiGunGuDto> siGunGuDtos = joinRepository.selectSiGunGuRelatedList(joinDto);
                        result.put("data", siGunGuDtos);
                    }else if(dataListDto.getDataListId() == ConstantUtil.DATA_LIST_EUPMYEONDONG_ID){
                        List<EupMyeonDongDto> eupMyeonDongDtos = joinRepository.selectEupMyeonDongRelatedList(joinDto);
                        result.put("data", eupMyeonDongDtos);
                    }else if(dataListDto.getDataListId() == ConstantUtil.DATA_LIST_LI_ID){
                        List<LiDto> liDtos = joinRepository.selectLiRelatedList(joinDto);
                        result.put("data", liDtos);
                    }else if(dataListDto.getDataListId() == ConstantUtil.DATA_LIST_ROAD_ID){
                        List<RoadDto> roadDtos = joinRepository.selectRoadRelatedList(joinDto);
                        result.put("data", roadDtos);
                    }

                    break;
                }
            }

            result.put("success", true);
            result.put("message", "Select Related List Success");
        }catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Select Related List Failed");
        }

        return result;
    }
}
