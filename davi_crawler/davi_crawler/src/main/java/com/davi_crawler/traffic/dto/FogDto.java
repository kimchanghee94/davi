package com.davi_crawler.traffic.dto;

import com.davi_crawler.traffic.entity.FogEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
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

    public FogEntity toEntity(){
        return FogEntity.builder()
                .fogId(fogId)
                .dataListId(dataListId)
                .poiSn(poiSn)
                .poiTyCode(poiTyCode)
                .roadTy(roadTy)
                .roadNo(roadNo)
                .poiNm(poiNm)
                .beginMapXCrdnt(beginMapXCrdnt)
                .beginMapYCrdnt(beginMapYCrdnt)
                .endMapXCrdnt(endMapXCrdnt)
                .endMapYCrdnt(endMapYCrdnt)
                .beginAdres(beginAdres)
                .endAdres(endAdres)
                .drcNm(drcNm)
                .sctnLt(sctnLt)
                .linkId(linkId)
                .remark(remark)
                .useAt(useAt)
                .registerId(registerId)
                .registDt(registDt)
                .updusrId(updusrId)
                .updtDt(updtDt)
                .dstrctId(dstrctId)
                .ledcd(ledcd)
                .centerMapYCrdnt(centerMapYCrdnt)
                .centerMapXCrdnt(centerMapXCrdnt)
                .build();
    }
}
