package com.davi_crawler.traffic.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.traffic.dto.TrafficLightDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(
        name = "TRAFFIC_LIGHT_SEQ",
        sequenceName = ConstantUtil.TRAFFICLIGHT_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "DV_TRAFFIC_TRAFFIC_LIGHT")
public class TrafficLightEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRAFFIC_LIGHT_SEQ")
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

    public TrafficLightDto toDto(){
        return TrafficLightDto.builder()
                .trafficLightId(trafficLightId)
                .dataListId(dataListId)
                .ctprvnNm(ctprvnNm)
                .signguNm(signguNm)
                .roadKnd(roadKnd)
                .roadRouteNo(roadRouteNo)
                .roadRouteNm(roadRouteNm)
                .roadRouteDrc(roadRouteDrc)
                .rdnmadr(rdnmadr)
                .lnmadr(lnmadr)
                .latitude(latitude)
                .longitude(longitude)
                .sgngnrInstlMthd(sgngnrInstlMthd)
                .roadType(roadType)
                .priorRoadYn(priorRoadYn)
                .tfclghtManageNo(tfclghtManageNo)
                .tfclghtSe(tfclghtSe)
                .tfclghtColorKnd(tfclghtColorKnd)
                .sgnaspMthd(sgnaspMthd)
                .sgnaspOrdr(sgnaspOrdr)
                .sgnaspTime(sgnaspTime)
                .sotKnd(sotKnd)
                .signlCtrlMthd(signlCtrlMthd)
                .signlTimeMthdType(signlTimeMthdType)
                .opratnYn(opratnYn)
                .flashingLightOpenHhmm(flashingLightOpenHhmm)
                .flashingLightCloseHhmm(flashingLightCloseHhmm)
                .fnctngSgngnrYn(fnctngSgngnrYn)
                .remndrIdctYn(remndrIdctYn)
                .sondSgngnrYn(sondSgngnrYn)
                .drcbrdSn(drcbrdSn)
                .institutionNm(institutionNm)
                .phoneNumber(phoneNumber)
                .referenceDate(referenceDate)
                .insttCode(insttCode)
                .build();
    }
}
