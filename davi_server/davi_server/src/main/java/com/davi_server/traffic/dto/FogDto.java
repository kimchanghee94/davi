package com.davi_server.traffic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FogDto {
    private Long fogId;
    private Long dataListId;
    private String poiSn;
    private String poiTyCode;
    private String roadTy;
    private String roadNo;
    private String poiNm;
    private String beginMapXCrdnt;
    private String beginMapYCrdnt;
    private String endMapXCrdnt;
    private String endMapYCrdnt;
    private String beginAdres;
    private String endAdres;
    private String drcNm;
    private String sctnLt;
    private String linkId;
    private String remark;
    private String useAt;
    private String registerId;
    private String registDt;
    private String updusrId;
    private String updtDt;
    private String dstrctId;
    private String ledcd;
    private String centerMapYCrdnt;
    private String centerMapXCrdnt;
}
