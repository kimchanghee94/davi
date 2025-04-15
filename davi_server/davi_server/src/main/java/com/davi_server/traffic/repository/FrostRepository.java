package com.davi_server.traffic.repository;

import com.davi_server.traffic.dto.FrostDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FrostRepository {
    List<FrostDto> selectSampleList() throws Exception;
    List<FrostDto> selectList() throws Exception;
}
