package com.davi_server.public_data_portal.repository;

import com.davi_server.public_data_portal.dto.PublicDataPortalDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PublicDataPortalRepository {
    List<PublicDataPortalDto> selectListByTableJsonFlag(PublicDataPortalDto publicDataPortalDto) throws Exception;
}
