package com.davi_server.public_data_portal.service;

import com.davi_server.public_data_portal.dto.PublicDataPortalDto;
import com.davi_server.public_data_portal.repository.PublicDataPortalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PublicDataPortalService {

    private final PublicDataPortalRepository publicDataPortalRepository;

    public Map<String, Object> selectListByTableJsonFlag(){
        Map<String, Object> result = new HashMap<>();

        try{
            PublicDataPortalDto publicDataPortalDto = new PublicDataPortalDto();
            publicDataPortalDto.setTableJsonFlag("table");
            List<PublicDataPortalDto> publicDataPortalDtos = publicDataPortalRepository.selectListByTableJsonFlag(publicDataPortalDto);

            result.put("success", true);
            result.put("message", "Get Public Data Portal List Success");
            result.put("data", publicDataPortalDtos);

            return result;
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("meesage", "Get Public Data Portal List Fail");
        }

        return result;
    }
}
