package com.davi_server.common.repository;

import com.davi_server.common.dto.DataListDto;
import com.davi_server.common.dto.DataColListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DataColListRepository {
    List<DataColListDto> selectByDataListId(DataListDto dataListDto) throws Exception;
}
