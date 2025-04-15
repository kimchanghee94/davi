package com.davi_server.molit.repository;

import com.davi_server.molit.dto.RoadDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoadRepository {
    List<RoadDto> selectSampleList() throws Exception;
    List<RoadDto> selectList() throws Exception;
}
