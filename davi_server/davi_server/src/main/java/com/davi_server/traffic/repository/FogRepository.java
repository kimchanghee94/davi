package com.davi_server.traffic.repository;

import com.davi_server.traffic.dto.FogDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FogRepository {
    List<FogDto> selectSampleList() throws Exception;
    List<FogDto> selectList() throws Exception;
}
