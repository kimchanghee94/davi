package com.davi_server.molit.repository;

import com.davi_server.molit.dto.EupMyeonDongDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EupMyeonDongRepository {
    List<EupMyeonDongDto> selectSampleList() throws Exception;
    List<EupMyeonDongDto> selectList(EupMyeonDongDto eupMyeonDongDto) throws Exception;
}
