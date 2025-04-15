package com.davi_crawler.common.dto;

import com.davi_crawler.common.entity.DataListEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataListDto {
    private Long dataListId;
    private String name;
    private String url;
    private String tableNm;
    private Integer metaType;
    private String period;
    private String lastUpdate;

    public DataListEntity toEntity(){
        return DataListEntity.builder()
                .dataListId(dataListId)
                .name(name)
                .url(url)
                .tableNm(tableNm)
                .metaType(metaType)
                .period(period)
                .lastUpdate(lastUpdate)
                .build();
    }
}
