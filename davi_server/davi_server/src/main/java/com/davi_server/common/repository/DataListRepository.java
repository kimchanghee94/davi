package com.davi_server.common.repository;

import com.davi_server.common.dto.DataListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DataListRepository {
    List<DataListDto> selectList() throws Exception;
    DataListDto selectById(DataListDto dataListDto) throws Exception;
    List<DataListDto> selectListByMetaType(DataListDto dataListDto) throws Exception;
}