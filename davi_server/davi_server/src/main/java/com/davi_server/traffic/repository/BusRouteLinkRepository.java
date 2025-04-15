package com.davi_server.traffic.repository;

import com.davi_server.traffic.dto.BusRouteLinkDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BusRouteLinkRepository {
    List<BusRouteLinkDto> selectSampleList(BusRouteLinkDto busRouteLinkDto) throws Exception;
    List<BusRouteLinkDto> selectList(BusRouteLinkDto busRouteLinkDto) throws Exception;
}
