package com.davi_server.traffic.repository;

import com.davi_server.traffic.dto.ChildZoneDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChildZoneRepository {
    List<ChildZoneDto> selectSampleList() throws Exception;
    List<ChildZoneDto> selectList() throws Exception;
}
