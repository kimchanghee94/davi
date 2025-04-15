package com.davi_server.traffic.repository;

import com.davi_server.traffic.dto.CctvDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CctvRepository {
    List<CctvDto> selectSampleList() throws Exception;
    List<CctvDto> selectList() throws Exception;
}
