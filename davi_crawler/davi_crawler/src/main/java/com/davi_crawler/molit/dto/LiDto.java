package com.davi_crawler.molit.dto;

import com.davi_crawler.molit.entity.LiEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiDto {
    private Long liId;
    private Long dataListId;
    private String liEngNm;
    private String liKorNm;
    private String fullNm;
    private String liCd;
    private String jsonData;

    public LiEntity toEntity(){
        return LiEntity.builder()
                .liId(liId)
                .dataListId(dataListId)
                .liEngNm(liEngNm)
                .liKorNm(liKorNm)
                .fullNm(fullNm)
                .liCd(liCd)
                .jsonData(jsonData)
                .build();
    }
}
