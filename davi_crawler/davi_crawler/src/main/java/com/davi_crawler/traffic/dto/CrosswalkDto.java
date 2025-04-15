package com.davi_crawler.traffic.dto;

import com.davi_crawler.traffic.entity.CrosswalkEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
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

    public CrosswalkEntity toEntity(){
        return CrosswalkEntity.builder()
                .crosswalkId(crosswalkId)
                .dataListId(dataListId)
                .ctprvnNm(ctprvnNm)
                .signguNm(signguNm)
                .roadNm(roadNm)
                .rdnmadr(rdnmadr)
                .lnmadr(lnmadr)
                .crslkManageNo(crslkManageNo)
                .crslkKnd(crslkKnd)
                .bcyclCrslkCmbnatYn(bcyclCrslkCmbnatYn)
                .highlandYn(highlandYn)
                .latitude(latitude)
                .longitude(longitude)
                .cartrkCo(cartrkCo)
                .bt(bt)
                .et(et)
                .tfclghtYn(tfclghtYn)
                .fnctngSgngnrYn(fnctngSgngnrYn)
                .sondSgngnrYn(sondSgngnrYn)
                .greenSgngnrTime(greenSgngnrTime)
                .redSgngnrTime(redSgngnrTime)
                .tfcilndYn(tfcilndYn)
                .ftpthLowerYn(ftpthLowerYn)
                .brllBlckYn(brllBlckYn)
                .cnctrLghtFcltyYn(cnctrLghtFcltyYn)
                .institutionNm(institutionNm)
                .phoneNumber(phoneNumber)
                .referenceDate(referenceDate)
                .insttCode(insttCode)
                .build();
    }
}