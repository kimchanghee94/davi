package com.davi_crawler.traffic.dto;

import com.davi_crawler.traffic.entity.BusRoutePathEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusRoutePathDto {
    private Long busRoutePathId;
    private Long dataListId;
    private String routeId;
    private String jsonData;

    public BusRoutePathEntity toEntity(){
        return BusRoutePathEntity.builder()
                .busRoutePathId(busRoutePathId)
                .dataListId(dataListId)
                .routeId(routeId)
                .jsonData(jsonData)
                .build();
    }
}
