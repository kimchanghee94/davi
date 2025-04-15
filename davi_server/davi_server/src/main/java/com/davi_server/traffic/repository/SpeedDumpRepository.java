package com.davi_server.traffic.repository;

import com.davi_server.traffic.dto.SpeedDumpDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SpeedDumpRepository {
    List<SpeedDumpDto> selectSampleList() throws Exception;
    List<SpeedDumpDto> selectList() throws Exception;
}
