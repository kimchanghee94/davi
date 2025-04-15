package com.davi_server.public_data_portal.service;

import com.davi_server.public_data_portal.dto.PublicDataPortalDto;
import com.davi_server.public_data_portal.dto.PublicDataPortalServiceDto;
import com.davi_server.public_data_portal.repository.PublicDataPortalServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PublicDataPortalServiceService {

    private final PublicDataPortalServiceRepository publicDataPortalServiceRepository;

    public Map<String, Object> selectListByPublicDataPortalId(PublicDataPortalDto publicDataPortalDto){
        Map<String, Object> result = new HashMap<>();

        try{
            PublicDataPortalServiceDto publicDataPortalServiceDto = new PublicDataPortalServiceDto();
            publicDataPortalServiceDto.setPublicDataPortalId(publicDataPortalDto.getPublicDataPortalId());

            List<PublicDataPortalServiceDto> publicDataPortalServiceDtos =
            publicDataPortalServiceRepository.selectListByPublicDataPortalId(publicDataPortalServiceDto);

            result.put("success", false);
            result.put("message", "Get Public Data Portal Service List Success");
            result.put("data", publicDataPortalServiceDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Public Data Portal Service List Fail");
        }

        return result;
    }
}
