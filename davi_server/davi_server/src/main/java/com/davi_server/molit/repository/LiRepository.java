package com.davi_server.molit.repository;

import com.davi_server.molit.dto.LiDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LiRepository {
    List<LiDto> selectSampleList() throws Exception;
    List<LiDto> selectList() throws Exception;
}
