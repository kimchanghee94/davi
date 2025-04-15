package com.davi_crawler.disaster.dto;

import com.davi_crawler.disaster.entity.ChimsuEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChimsuDto {
    private Long chimsuId;
    private Long dataListId;
    @JsonProperty(value = "objectid")
    private String objectId;
    private String guNam;
    @JsonProperty(value = "f_shim")
    private String fShim;
    @JsonProperty(value = "f_disa_nm")
    private String fDisaNm;
    @JsonProperty(value = "f_avr_hgt")
    private String fAvrHgt;
    @JsonProperty(value = "f_area")
    private String fArea;
    private String admCd;
    @JsonProperty(value = "f_sat_ymd")
    private String fSatYmd;
    @JsonProperty(value = "f_end_ymd")
    private String fEndYmd;
    @JsonProperty(value = "f_sat_tm")
    private String fSatTm;
    @JsonProperty(value = "f_end_tm")
    private String fEndTm;
    @JsonProperty(value = "f_rsn_dtl")
    private String fRsnDtl;
    @JsonProperty(value = "f_zone_nm")
    private String fZoneNm;
    private String invYr;
    @JsonProperty(value = "f_yr")
    private String fYr;
    private String type;
    private String jsonData;

    public ChimsuEntity toEntity(){
        return ChimsuEntity.builder()
                .chimsuId(chimsuId)
                .dataListId(dataListId)
                .objectId(objectId)
                .guNam(guNam)
                .fShim(fShim)
                .fDisaNm(fDisaNm)
                .fAvrHgt(fAvrHgt)
                .fArea(fArea)
                .admCd(admCd)
                .fSatYmd(fSatYmd)
                .fEndYmd(fEndYmd)
                .fSatTm(fSatTm)
                .fEndTm(fEndTm)
                .fRsnDtl(fRsnDtl)
                .fZoneNm(fZoneNm)
                .invYr(invYr)
                .fYr(fYr)
                .type(type)
                .jsonData(jsonData)
                .build();
    }
}
