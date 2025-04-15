package com.davi_crawler.khoa.dto;

import com.davi_crawler.khoa.entity.HangEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HangDto {
    private Long hangId;
    private Long dataListId;
    private String hangName;
    private String hangSubName;
    private String lngLat;

    public HangEntity toEntity(){
        return HangEntity.builder()
                .hangId(hangId)
                .dataListId(dataListId)
                .hangName(hangName)
                .hangSubName(hangSubName)
                .lngLat(lngLat)
                .build();
    }
}
