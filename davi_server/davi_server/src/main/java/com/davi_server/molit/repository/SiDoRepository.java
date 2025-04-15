package com.davi_server.molit.repository;

import com.davi_server.molit.dto.SiDoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SiDoRepository {
    List<SiDoDto> selectSampleList() throws Exception;
    List<SiDoDto> selectList() throws Exception;
}
