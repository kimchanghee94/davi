package com.davi_server.traffic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusSeoulInfoDto {
    private Long busSeoulInfoId;
    private Long dataListId;
    private String routeId;
    private String routeNm;
    private String nodeOrder;
    private String nodeId;
    private String arsId;
    private String busStaNm;
    private String longitude;
    private String latitude;
}
