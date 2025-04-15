package com.davi_server.traffic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrafficLightDto {
    private Long trafficLightId;
    private Long dataListId;
    private String ctprvnNm;
    private String signguNm;
    private String roadKnd;
    private String roadRouteNo;
    private String roadRouteNm;
    private String roadRouteDrc;
    private String rdnmadr;
    private String lnmadr;
    private String latitude;
    private String longitude;
    private String sgngnrInstlMthd;
    private String roadType;
    private String priorRoadYn;
    private String tfclghtManageNo;
    private String tfclghtSe;
    private String tfclghtColorKnd;
    private String sgnaspMthd;
    private String sgnaspOrdr;
    private String sgnaspTime;
    private String sotKnd;
    private String signlCtrlMthd;
    private String signlTimeMthdType;
    private String opratnYn;
    private String flashingLightOpenHhmm;
    private String flashingLightCloseHhmm;
    private String fnctngSgngnrYn;
    private String remndrIdctYn;
    private String sondSgngnrYn;
    private String drcbrdSn;
    private String institutionNm;
    private String phoneNumber;
    private String referenceDate;
    private String insttCode;
}
