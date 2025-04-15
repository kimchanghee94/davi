package com.davi_crawler.molit.dto;

import com.davi_crawler.molit.entity.SiDoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiDoDto {
    private Long siDoId;
    private Long dataListId;
    private String ctpEngNm;
    private String ctpKorNm;
    private String ctprvnCd;
    private String jsonData;

    public SiDoEntity toEntity(){
        return SiDoEntity.builder()
                .siDoId(siDoId)
                .dataListId(dataListId)
                .ctpEngNm(ctpEngNm)
                .ctpKorNm(ctpKorNm)
                .ctprvnCd(ctprvnCd)
                .jsonData(jsonData)
                .build();
    }
}
