package com.davi_server.common.repository;

import com.davi_server.common.dto.JoinDto;
import com.davi_server.molit.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JoinRepository {
    List<EupMyeonDongDto> selectEupMyeonDongRelatedList(JoinDto joinDto) throws Exception;
    List<LiDto> selectLiRelatedList(JoinDto joinDto) throws Exception;
    List<LinkDto> selectLinkRelatedList(JoinDto joinDto) throws Exception;
    List<RoadDto> selectRoadRelatedList(JoinDto joinDto) throws Exception;
    List<SiDoDto> selectSiDoRelatedList(JoinDto joinDto) throws Exception;
    List<SiGunGuDto> selectSiGunGuRelatedList(JoinDto joinDto) throws Exception;
}
