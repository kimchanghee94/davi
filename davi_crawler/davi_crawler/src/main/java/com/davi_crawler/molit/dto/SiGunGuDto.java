package com.davi_crawler.molit.dto;

import com.davi_crawler.molit.entity.SiGunGuEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiGunGuDto {
    private Long siGunGuId;
    private Long dataListId;
    private String sigEngNm;
    private String sigKorNm;
    private String fullNm;
    private String sigCd;
    private String jsonData;
    private String centerLat;
    private String centerLng;

    public SiGunGuEntity toEntity(){
        return SiGunGuEntity.builder()
                .siGunGuId(siGunGuId)
                .dataListId(dataListId)
                .sigEngNm(sigEngNm)
                .sigKorNm(sigKorNm)
                .fullNm(fullNm)
                .sigCd(sigCd)
                .jsonData(jsonData)
                .centerLat(centerLat)
                .centerLng(centerLng)
                .build();
    }
}
