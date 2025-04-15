package com.davi_server.traffic.repository;

import com.davi_server.traffic.dto.BusSeoulInfoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BusSeoulInfoRepository {
    List<BusSeoulInfoDto> selectSampleList(BusSeoulInfoDto busSeoulInfoDto) throws Exception;
    List<BusSeoulInfoDto> selectList(BusSeoulInfoDto busSeoulInfoDto) throws Exception;
}
