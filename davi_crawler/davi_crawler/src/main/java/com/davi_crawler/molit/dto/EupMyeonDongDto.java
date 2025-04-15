package com.davi_crawler.molit.dto;

import com.davi_crawler.molit.entity.EupMyeonDongEntity;
import com.davi_crawler.molit.entity.SiGunGuEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EupMyeonDongDto {
    private Long eupMyeonDongId;
    private Long dataListId;
    private String emdEngNm;
    private String emdKorNm;
    private String fullNm;
    private String emdCd;
    private String jsonData;
    private String hdongCd;
    private String bdongCd;
    private String centerLat;
    private String centerLng;

    public EupMyeonDongEntity toEntity(){
        return EupMyeonDongEntity.builder()
                .eupMyeonDongId(eupMyeonDongId)
                .dataListId(dataListId)
                .emdEngNm(emdEngNm)
                .emdKorNm(emdKorNm)
                .fullNm(fullNm)
                .emdCd(emdCd)
                .jsonData(jsonData)
                .hdongCd(hdongCd)
                .bdongCd(bdongCd)
                .centerLat(centerLat)
                .centerLng(centerLng)
                .build();
    }
}
