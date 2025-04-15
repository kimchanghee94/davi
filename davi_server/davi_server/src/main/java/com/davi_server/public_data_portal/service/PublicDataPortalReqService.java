package com.davi_server.public_data_portal.service;

import com.davi_server.public_data_portal.dto.PublicDataPortalReqDto;
import com.davi_server.public_data_portal.dto.PublicDataPortalServiceDto;
import com.davi_server.public_data_portal.repository.PublicDataPortalReqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PublicDataPortalReqService {

    private final PublicDataPortalReqRepository publicDataPortalReqRepository;

    public Map<String, Object> selectListByPublicDataPortalServiceId(PublicDataPortalServiceDto publicDataPortalServiceDto){
        Map<String, Object> result = new HashMap<>();

        try{
            PublicDataPortalReqDto publicDataPortalReqDto = new PublicDataPortalReqDto();
            publicDataPortalReqDto.setPublicDataPortalServiceId(publicDataPortalServiceDto.getPublicDataPortalServiceId());

            List<PublicDataPortalReqDto> publicDataPortalReqDtos
                    = publicDataPortalReqRepository.selectListByPublicDataPortalServiceId(publicDataPortalReqDto);

            result.put("success", true);
            result.put("message", "Get Public Data Portal Req List Success");
            result.put("data", publicDataPortalReqDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Public Data Portal Req List Fail");
        }

        return result;
    }
}
