package com.davi_server.traffic.repository;

import com.davi_server.traffic.dto.TrafficLightDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TrafficLightRepository {
    List<TrafficLightDto> selectSampleList() throws Exception;
    List<TrafficLightDto> selectList() throws Exception;
}
