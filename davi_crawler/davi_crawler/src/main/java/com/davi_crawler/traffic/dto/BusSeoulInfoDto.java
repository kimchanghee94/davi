package com.davi_crawler.traffic.dto;

import com.davi_crawler.traffic.entity.BusSeoulInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
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

    public BusSeoulInfoEntity toEntity(){
        return BusSeoulInfoEntity.builder()
                .busSeoulInfoId(busSeoulInfoId)
                .dataListId(dataListId)
                .routeId(routeId)
                .routeNm(routeNm)
                .nodeOrder(nodeOrder)
                .nodeId(nodeId)
                .arsId(arsId)
                .busStaNm(busStaNm)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }
}
