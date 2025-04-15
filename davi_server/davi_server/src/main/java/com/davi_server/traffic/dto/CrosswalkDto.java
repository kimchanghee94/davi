package com.davi_server.traffic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrosswalkDto {
    private Long crosswalkId;
    private Long dataListId;
    private String ctprvnNm;
    private String signguNm;
    private String roadNm;
    private String rdnmadr;
    private String lnmadr;
    private String crslkManageNo;
    private String crslkKnd;
    private String bcyclCrslkCmbnatYn;
    private String highlandYn;
    private String latitude;
    private String longitude;
    private String cartrkCo;
    private String bt;
    private String et;
    private String tfclghtYn;
    private String fnctngSgngnrYn;
    private String sondSgngnrYn;
    private String greenSgngnrTime;
    private String redSgngnrTime;
    private String tfcilndYn;
    private String ftpthLowerYn;
    private String brllBlckYn;
    private String cnctrLghtFcltyYn;
    private String institutionNm;
    private String phoneNumber;
    private String referenceDate;
    private String insttCode;
}
