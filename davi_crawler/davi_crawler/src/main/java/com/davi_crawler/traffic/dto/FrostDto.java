package com.davi_crawler.traffic.dto;

import com.davi_crawler.traffic.entity.FrostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrostDto {
    private Long frostId;
    private Long dataListId;
    private String secId;
    private String administration;
    private String roadDiv;
    private String repArea;
    private String roadNm;
    private String totLen;
    private String strLat;
    private String strLng;
    private String endLat;
    private String endLng;
    private String roadDirAngleX;
    private String roadDirAngleY;
    private String roadDirAngleBaseDecBea;

    public FrostEntity toEntity(){
        return FrostEntity.builder()
                .frostId(frostId)
                .dataListId(dataListId)
                .secId(secId)
                .administration(administration)
                .roadDiv(roadDiv)
                .repArea(repArea)
                .roadNm(roadNm)
                .totLen(totLen)
                .strLat(strLat)
                .strLng(strLng)
                .endLat(endLat)
                .endLng(endLng)
                .roadDirAngleX(roadDirAngleX)
                .roadDirAngleY(roadDirAngleY)
                .roadDirAngleBaseDecBea(roadDirAngleBaseDecBea)
                .build();
    }
}
