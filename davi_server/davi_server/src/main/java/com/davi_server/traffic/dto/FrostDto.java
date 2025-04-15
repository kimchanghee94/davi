package com.davi_server.traffic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
}
