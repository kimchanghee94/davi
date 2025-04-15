package com.davi_server.traffic.repository;

import com.davi_server.traffic.dto.RseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RseRepository {
    List<RseDto> selectSampleList() throws Exception;
    List<RseDto> selectList() throws Exception;
}
