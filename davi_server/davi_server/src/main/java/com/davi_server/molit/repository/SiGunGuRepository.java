package com.davi_server.molit.repository;

import com.davi_server.molit.dto.SiGunGuDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SiGunGuRepository {
    List<SiGunGuDto> selectSampleList() throws Exception;
    List<SiGunGuDto> selectList(SiGunGuDto siGunGuDto) throws Exception;
}