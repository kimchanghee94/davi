package com.davi_server.khoa.repository;

import com.davi_server.khoa.dto.SeaDistDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SeaDistRepository {
    List<SeaDistDto> selectSampleList() throws Exception;
    List<SeaDistDto> selectList() throws Exception;

}
