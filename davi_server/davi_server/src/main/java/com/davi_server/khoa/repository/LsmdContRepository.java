package com.davi_server.khoa.repository;

import com.davi_server.khoa.dto.HangDto;
import com.davi_server.khoa.dto.LsmdContDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LsmdContRepository {
    List<LsmdContDto> selectSampleList() throws Exception;
    List<LsmdContDto> selectList() throws Exception;
}
