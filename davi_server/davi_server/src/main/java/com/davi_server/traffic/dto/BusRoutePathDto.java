package com.davi_server.traffic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusRoutePathDto {
    private Long busRoutePathId;
    private Long dataListId;
    private String routeId;
    private String jsonData;
}
