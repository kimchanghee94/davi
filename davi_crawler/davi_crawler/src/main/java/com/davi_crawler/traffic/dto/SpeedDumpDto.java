package com.davi_crawler.traffic.dto;

import com.davi_crawler.traffic.entity.SpeedDumpEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
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

    public SpeedDumpEntity toEntity(){
        return SpeedDumpEntity.builder()
                .speedDumpId(speedDumpId)
                .dataListId(dataListId)
                .speedDumpManageNo(speedDumpManageNo)
                .roadNm(roadNm)
                .ctprvnNm(ctprvnNm)
                .signguNm(signguNm)
                .rdnmadr(rdnmadr)
                .lnmadr(lnmadr)
                .itlpc(itlpc)
                .speedDumpIngredient(speedDumpIngredient)
                .speedDumpShape(speedDumpShape)
                .ht(ht)
                .bt(bt)
                .et(et)
                .roadKnd(roadKnd)
                .standYn(standYn)
                .latitude(latitude)
                .longitude(longitude)
                .sepCarYn(sepCarYn)
                .contYn(contYn)
                .installationYear(installationYear)
                .institutionNm(institutionNm)
                .phoneNumber(phoneNumber)
                .referenceDate(referenceDate)
                .build();
    }
}
