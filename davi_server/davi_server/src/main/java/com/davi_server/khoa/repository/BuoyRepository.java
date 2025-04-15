package com.davi_server.khoa.repository;

import com.davi_server.khoa.dto.BuoyDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BuoyRepository {
    List<BuoyDto> selectSampleList() throws Exception;
    List<BuoyDto> selectList() throws Exception;
    List<BuoyDto> selectClickPosList(Map<String, Object> paramDto) throws Exception;
}
