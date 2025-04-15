package com.davi_crawler.traffic.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.traffic.dto.CctvDto;
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
        name = "CCTV_SEQ",
        sequenceName = ConstantUtil.CCTV_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name="DV_TRAFFIC_CCTV")
public class CctvEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CCTV_SEQ")
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
    private String lnmadr;
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

    public CctvDto toDto(){
        return CctvDto.builder()
                .cctvId(cctvId)
                .dataListId(dataListId)
                .mnlssRegltCameraManageNo(mnlssRegltCameraManageNo)
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
                .itlpc(itlpc)
                .regltSe(regltSe)
                .lmttVe(lmttVe)
                .regltSctnLcSe(regltSctnLcSe)
                .ovrspdRegltSctnLt(ovrspdRegltSctnLt)
                .prtcareaType(prtcareaType)
                .installationYear(installationYear)
                .institutionNm(institutionNm)
                .phoneNumber(phoneNumber)
                .referenceDate(referenceDate)
                .insttCode(insttCode)
                .build();
    }
}
