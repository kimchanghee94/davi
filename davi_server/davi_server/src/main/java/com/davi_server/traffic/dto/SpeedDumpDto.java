package com.davi_server.traffic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeedDumpDto {
    private Long speedDumpId;
    private Long dataListId;
    private String speedDumpManageNo;
    private String roadNm;
    private String ctprvnNm;
    private String signguNm;
    private String rdnmadr;
    private String lnmadr;
    private String itlpc;
    private String speedDumpIngredient;
    private String speedDumpShape;
    private String ht;
    private String bt;
    private String et;
    private String roadKnd;
    private String standYn;
    private String latitude;
    private String longitude;
    private String sepCarYn;
    private String contYn;
    private String installationYear;
    private String institutionNm;
    private String phoneNumber;
    private String referenceDate;
}
