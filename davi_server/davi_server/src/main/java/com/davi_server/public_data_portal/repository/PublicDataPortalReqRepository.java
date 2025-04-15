package com.davi_server.public_data_portal.repository;

import com.davi_server.public_data_portal.dto.PublicDataPortalReqDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PublicDataPortalReqRepository {
    List<PublicDataPortalReqDto> selectListByPublicDataPortalServiceId(PublicDataPortalReqDto publicDataPortalReqDto) throws Exception;
}