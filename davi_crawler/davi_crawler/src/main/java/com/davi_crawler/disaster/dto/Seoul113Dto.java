package com.davi_crawler.disaster.dto;

import com.davi_crawler.disaster.entity.Seoul113Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seoul113Dto {
    private Long seoul113Id;
    private Long dataListId;
    private String category;
    private String areaCd;
    private String areaNm;
    private String jsonData;

    public Seoul113Entity toEntity(){
        return Seoul113Entity.builder()
                .seoul113Id(seoul113Id)
                .dataListId(dataListId)
                .category(category)
                .areaCd(areaCd)
                .areaNm(areaNm)
                .jsonData(jsonData)
                .build();
    }
}
