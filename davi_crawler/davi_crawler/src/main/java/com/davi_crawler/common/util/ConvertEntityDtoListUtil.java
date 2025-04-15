package com.davi_crawler.common.util;

import com.davi_crawler.common.dto.DataColListDto;
import com.davi_crawler.common.dto.DataListDto;
import com.davi_crawler.common.dto.GridDto;
import com.davi_crawler.common.entity.DataColListEntity;
import com.davi_crawler.common.entity.DataListEntity;
import com.davi_crawler.common.entity.GridEntity;
import com.davi_crawler.disaster.dto.ChimsuDto;
import com.davi_crawler.disaster.dto.CivilDefenseDto;
import com.davi_crawler.disaster.dto.Seoul113Dto;
import com.davi_crawler.disaster.entity.ChimsuEntity;
import com.davi_crawler.disaster.entity.CivilDefenseEntity;
import com.davi_crawler.disaster.entity.Seoul113Entity;
import com.davi_crawler.khoa.dto.BuoyDto;
import com.davi_crawler.khoa.dto.HangDto;
import com.davi_crawler.khoa.dto.LsmdContDto;
import com.davi_crawler.khoa.dto.SeaDistDto;
import com.davi_crawler.khoa.entity.BuoyEntity;
import com.davi_crawler.khoa.entity.HangEntity;
import com.davi_crawler.khoa.entity.LsmdContEntity;
import com.davi_crawler.khoa.entity.SeaDistEntity;
import com.davi_crawler.molit.dto.*;
import com.davi_crawler.molit.entity.*;
import com.davi_crawler.public_data_portal.dto.PublicDataPortalDto;
import com.davi_crawler.public_data_portal.dto.PublicDataPortalReqDto;
import com.davi_crawler.public_data_portal.dto.PublicDataPortalResDto;
import com.davi_crawler.public_data_portal.dto.PublicDataPortalServiceDto;
import com.davi_crawler.public_data_portal.entity.PublicDataPortalEntity;
import com.davi_crawler.public_data_portal.entity.PublicDataPortalReqEntity;
import com.davi_crawler.public_data_portal.entity.PublicDataPortalResEntity;
import com.davi_crawler.public_data_portal.entity.PublicDataPortalServiceEntity;
import com.davi_crawler.traffic.dto.*;
import com.davi_crawler.traffic.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class ConvertEntityDtoListUtil {

    /**
     * @param list CrawlerEntity 리스트
     * @return CrawlerDto 리스트로 반환
     */
    public static List<DataListDto> toListDataListDto(List<DataListEntity> list){
        return list.stream()
                .map(DataListEntity::toDto)
                .collect(Collectors.toList());
    }

    /**
     * @param list CrawlerDto 리스트
     * @return CrawlerEntity 리스트로 반환
     */
    public static List<DataListEntity> toListDataListEntity(List<DataListDto> list){
        return list.stream()
                .map(DataListDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<PublicDataPortalDto> toListPublicDataPortalDto(List<PublicDataPortalEntity> list){
        return list.stream()
                .map(PublicDataPortalEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<PublicDataPortalEntity> toListPublicDataPortalEntity(List<PublicDataPortalDto> list){
        return list.stream()
                .map(PublicDataPortalDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<PublicDataPortalServiceDto> toListPublicDataPortalServiceDto(List<PublicDataPortalServiceEntity> list){
        return list.stream()
                .map(PublicDataPortalServiceEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<PublicDataPortalServiceEntity> toListPublicDataPortalServiceEntity(List<PublicDataPortalServiceDto> list){
        return list.stream()
                .map(PublicDataPortalServiceDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<PublicDataPortalReqDto> toListPublicDataPortalReqDto(List<PublicDataPortalReqEntity> list){
        return list.stream()
                .map(PublicDataPortalReqEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<PublicDataPortalReqEntity> toListPublicDataPortalReqEntity(List<PublicDataPortalReqDto> list){
        return list.stream()
                .map(PublicDataPortalReqDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<PublicDataPortalResDto> toListPublicDataPortalResDto(List<PublicDataPortalResEntity> list){
        return list.stream()
                .map(PublicDataPortalResEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<PublicDataPortalResEntity> toListPublicDataPortalResEntity(List<PublicDataPortalResDto> list){
        return list.stream()
                .map(PublicDataPortalResDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<SiDoDto> toListSiDoDto(List<SiDoEntity> list){
        return list.stream()
                .map(SiDoEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<SiDoEntity> toListSiDoEntity(List<SiDoDto> list){
        return list.stream()
                .map(SiDoDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<SiGunGuDto> toListSiGunGuDto(List<SiGunGuEntity> list){
        return list.stream()
                .map(SiGunGuEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<SiGunGuEntity> toListSiGunGuEntity(List<SiGunGuDto> list){
        return list.stream()
                .map(SiGunGuDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<EupMyeonDongDto> toListEupMyeonDongDto(List<EupMyeonDongEntity> list){
        return list.stream()
                .map(EupMyeonDongEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<EupMyeonDongEntity> toListEupMyeonDongEntity(List<EupMyeonDongDto> list){
        return list.stream()
                .map(EupMyeonDongDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<LiDto> toListLiDto(List<LiEntity> list){
        return list.stream()
                .map(LiEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<LiEntity> toListLiEntity(List<LiDto> list){
        return list.stream()
                .map(LiDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<LinkDto> toListLinkDto(List<LinkEntity> list){
        return list.stream()
                .map(LinkEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<LinkEntity> toListLinkEntity(List<LinkDto> list){
        return list.stream()
                .map(LinkDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<RoadDto> toListRoadDto(List<RoadEntity> list){
        return list.stream()
                .map(RoadEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<RoadEntity> toListRoadEntity(List<RoadDto> list){
        return list.stream()
                .map(RoadDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<SeaDistDto> toListSeaDistDto(List<SeaDistEntity> list){
        return list.stream()
                .map(SeaDistEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<SeaDistEntity> toListSeaDistEntity(List<SeaDistDto> list){
        return list.stream()
                .map(SeaDistDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<HangDto> toListHangDto(List<HangEntity> list){
        return list.stream()
                .map(HangEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<HangEntity> toListHangEntity(List<HangDto> list){
        return list.stream()
                .map(HangDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<BuoyDto> toListBuoyDto(List<BuoyEntity> list){
        return list.stream()
                .map(BuoyEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<BuoyEntity> toListBuoyEntity(List<BuoyDto> list){
        return list.stream()
                .map(BuoyDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<LsmdContDto> toListLsmdContDto(List<LsmdContEntity> list){
        return list.stream()
                .map(LsmdContEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<LsmdContEntity> toListLsmdContEntity(List<LsmdContDto> list){
        return list.stream()
                .map(LsmdContDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<CctvDto> toListCctvDto(List<CctvEntity> list){
        return list.stream()
                .map(CctvEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<CctvEntity> toListCctvEntity(List<CctvDto> list){
        return list.stream()
                .map(CctvDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<ChildZoneDto> toListChildZoneDto(List<ChildZoneEntity> list){
        return list.stream()
                .map(ChildZoneEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<ChildZoneEntity> toListChildZoneEntity(List<ChildZoneDto> list){
        return list.stream()
                .map(ChildZoneDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<DataColListDto> toListDataColListDto(List<DataColListEntity> list){
        return list.stream()
                .map(DataColListEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<DataColListEntity> toListDataColListEntity(List<DataColListDto> list){
        return list.stream()
                .map(DataColListDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<TrafficLightDto> toListTrafficLightDto(List<TrafficLightEntity> list){
        return list.stream()
                .map(TrafficLightEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<TrafficLightEntity> toListTrafficLightEntity(List<TrafficLightDto> list){
        return list.stream()
                .map(TrafficLightDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<CrosswalkDto> toListCrosswalkDto(List<CrosswalkEntity> list){
        return list.stream()
                .map(CrosswalkEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<CrosswalkEntity> toListCrosswalkEntity(List<CrosswalkDto> list){
        return list.stream()
                .map(CrosswalkDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<SpeedDumpDto> toListSpeedDumpDto(List<SpeedDumpEntity> list){
        return list.stream()
                .map(SpeedDumpEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<SpeedDumpEntity> toListSpeedDumpEntity(List<SpeedDumpDto> list){
        return list.stream()
                .map(SpeedDumpDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<RseDto> toListRseDto(List<RseEntity> list){
        return list.stream()
                .map(RseEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<RseEntity> toListRseEntity(List<RseDto> list){
        return list.stream()
                .map(RseDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<FrostDto> toListFrostDto(List<FrostEntity> list){
        return list.stream()
                .map(FrostEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<FrostEntity> toListFrostEntity(List<FrostDto> list){
        return list.stream()
                .map(FrostDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<FogDto> toListFogDto(List<FogEntity> list){
        return list.stream()
                .map(FogEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<FogEntity> toListFogEntity(List<FogDto> list){
        return list.stream()
                .map(FogDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<BusRouteLinkDto> toListBusRouteLinkDto(List<BusRouteLinkEntity> list){
        return list.stream()
                .map(BusRouteLinkEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<BusRouteLinkEntity> toListBusRouteLinkEntity(List<BusRouteLinkDto> list){
        return list.stream()
                .map(BusRouteLinkDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<BusSeoulInfoDto> toListBusSeoulInfoDto(List<BusSeoulInfoEntity> list){
        return list.stream()
                .map(BusSeoulInfoEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<BusSeoulInfoEntity> toListBusSeoulInfoEntity(List<BusSeoulInfoDto> list){
        return list.stream()
                .map(BusSeoulInfoDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<BusRoutePathDto> toListBusRoutePathDto(List<BusRoutePathEntity> list){
        return list.stream()
                .map(BusRoutePathEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<BusRoutePathEntity> toListBusRoutePathEntity(List<BusRoutePathDto> list){
        return list.stream()
                .map(BusRoutePathDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<CivilDefenseDto> toListCivilDefenseDto(List<CivilDefenseEntity> list){
        return list.stream()
                .map(CivilDefenseEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<CivilDefenseEntity> toListCivilDefenseEntity(List<CivilDefenseDto> list){
        return list.stream()
                .map(CivilDefenseDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<Seoul113Dto> toListSeoul113Dto(List<Seoul113Entity> list){
        return list.stream()
                .map(Seoul113Entity::toDto)
                .collect(Collectors.toList());
    }

    public static List<Seoul113Entity> toListSeoul113Entity(List<Seoul113Dto> list){
        return list.stream()
                .map(Seoul113Dto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<GridDto> toListGridDto(List<GridEntity> list){
        return list.stream()
                .map(GridEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<GridEntity> toListGridEntity(List<GridDto> list){
        return list.stream()
                .map(GridDto::toEntity)
                .collect(Collectors.toList());
    }

    public static List<ChimsuDto> toListChimsuDto(List<ChimsuEntity> list){
        return list.stream()
                .map(ChimsuEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<ChimsuEntity> toListChimsuEntity(List<ChimsuDto> list){
        return list.stream()
                .map(ChimsuDto::toEntity)
                .collect(Collectors.toList());
    }
}