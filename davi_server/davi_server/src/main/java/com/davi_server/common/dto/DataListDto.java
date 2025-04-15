package com.davi_server.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
}
