package com.davi_crawler.traffic.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.traffic.dto.FrostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(
        name = "FROST_SEQ",
        sequenceName = ConstantUtil.FROST_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "DV_TRAFFIC_FROST")
public class FrostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FROST_SEQ")
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
    @Column(name="road_dir_angle_x")
    private String roadDirAngleX;
    @Column(name="road_dir_angle_y")
    private String roadDirAngleY;
    private String roadDirAngleBaseDecBea;

    public FrostDto toDto(){
        return FrostDto.builder()
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
