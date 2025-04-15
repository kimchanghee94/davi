package com.davi_server.public_data_portal.service;

import com.davi_server.public_data_portal.dto.PublicDataPortalResDto;
import com.davi_server.public_data_portal.dto.PublicDataPortalServiceDto;
import com.davi_server.public_data_portal.repository.PublicDataPortalResRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PublicDataPortalResService {

    private final PublicDataPortalResRepository publicDataPortalResRepository;

    public Map<String, Object> selectListByPublicDataPortalServiceId(PublicDataPortalServiceDto publicDataPortalServiceDto){
        Map<String, Object> result = new HashMap<>();

        try{
            PublicDataPortalResDto publicDataPortalResDto = new PublicDataPortalResDto();
            publicDataPortalResDto.setPublicDataPortalServiceId(publicDataPortalServiceDto.getPublicDataPortalServiceId());

            List<PublicDataPortalResDto> publicDataPortalResDtos
                    = publicDataPortalResRepository.selectListByPublicDataPortalServiceId(publicDataPortalResDto);

            result.put("success", false);
            result.put("message", "Get Public Data Portal Res List Success");
            result.put("data", publicDataPortalResDtos);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Get Public Data Portal Res List Fail");
        }


        return result;
    }
}
