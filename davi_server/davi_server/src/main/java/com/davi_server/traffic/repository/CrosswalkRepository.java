package com.davi_server.traffic.repository;

import com.davi_server.traffic.dto.CrosswalkDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CrosswalkRepository {
    List<CrosswalkDto> selectSampleList() throws Exception;
    List<CrosswalkDto> selectList() throws Exception;
}
