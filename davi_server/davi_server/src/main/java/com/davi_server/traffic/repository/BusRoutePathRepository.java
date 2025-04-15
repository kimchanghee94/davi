package com.davi_server.traffic.repository;

import com.davi_server.traffic.dto.BusRoutePathDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BusRoutePathRepository {
    List<BusRoutePathDto> selectList(BusRoutePathDto busRoutePathDto) throws Exception;
}