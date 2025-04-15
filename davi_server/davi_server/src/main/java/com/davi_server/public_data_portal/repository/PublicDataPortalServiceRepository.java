package com.davi_server.public_data_portal.repository;

import com.davi_server.public_data_portal.dto.PublicDataPortalServiceDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PublicDataPortalServiceRepository {
    List<PublicDataPortalServiceDto> selectListByPublicDataPortalId(PublicDataPortalServiceDto publicDataPortalServiceDto) throws Exception;
}
