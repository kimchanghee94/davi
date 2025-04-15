package com.davi_crawler.common.dto;

import com.davi_crawler.common.entity.GridEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GridDto {
    private Long gridId;
    private Long dataListId;
    private String gridLevel;
    private String centerLat;
    private String centerLng;
    private String jsonData;

    public GridEntity toEntity(){
        return GridEntity.builder()
                .gridId(gridId)
                .dataListId(dataListId)
                .gridLevel(gridLevel)
                .centerLat(centerLat)
                .centerLng(centerLng)
                .jsonData(jsonData)
                .build();
    }
}
