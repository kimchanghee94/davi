package com.davi_server.khoa.repository;

import com.davi_server.khoa.dto.HangDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface HangRepository {
    List<HangDto> selectSampleList() throws Exception;
    List<HangDto> selectList() throws Exception;
    List<HangDto> selectClickPosList(Map<String, Object> paramDto) throws Exception;
}
