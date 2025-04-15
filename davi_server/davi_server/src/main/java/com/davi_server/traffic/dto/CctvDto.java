package com.davi_server.traffic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CctvDto {
    private Long cctvId;
    private Long dataListId;
    private String mnlssRegltCameraManageNo;
    private String ctprvnNm;
    private String signguNm;
    private String roadKnd;
    private String roadRouteNo;
    private String roadRouteNm;
    private String roadRouteDrc;
    private String rdnmadr;
    private String ldnmadr;
    private String latitude;
    private String longitude;
    private String itlpc;
    private String regltSe;
    private String lmttVe;
    private String regltSctnLcSe;
    private String ovrspdRegltSctnLt;
    private String prtcareaType;
    private String installationYear;
    private String institutionNm;
    private String phoneNumber;
    private String referenceDate;
    private String insttCode;
}