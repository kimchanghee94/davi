package com.davi_server.molit.repository;

import com.davi_server.molit.dto.LinkDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LinkRepository {
    LinkDto selectByPropLinkId(LinkDto linkDto) throws Exception;

    List<LinkDto> selectSampleList() throws Exception;
    List<LinkDto> selectList() throws Exception;
}
