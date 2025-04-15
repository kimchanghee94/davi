package com.davi_server.public_data_portal.repository;

import com.davi_server.public_data_portal.dto.PublicDataPortalResDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PublicDataPortalResRepository {
    List<PublicDataPortalResDto> selectListByPublicDataPortalServiceId(PublicDataPortalResDto publicDataPortalResDto) throws Exception;
}
