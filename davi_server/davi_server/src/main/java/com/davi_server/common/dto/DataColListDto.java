package com.davi_server.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataColListDto extends BasicDto {
    private Long colListId;
    private Long dataListId;
    private String colName;
    private String colDescript;
    private Integer order;
}
