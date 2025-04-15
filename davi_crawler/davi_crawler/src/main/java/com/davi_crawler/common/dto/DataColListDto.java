package com.davi_crawler.common.dto;

import com.davi_crawler.common.entity.DataColListEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataColListDto {
    private Long colListId;
    private Long dataListId;
    private String colName;
    private String colDescript;
    @Column(name="\"order\"")
    private Long order;

    public DataColListEntity toEntity(){
        return DataColListEntity.builder()
                .colListId(colListId)
                .dataListId(dataListId)
                .colName(colName)
                .colDescript(colDescript)
                .order(order)
                .build();
    }
}
