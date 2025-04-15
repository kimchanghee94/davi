package com.davi_crawler.traffic.dto;

import com.davi_crawler.traffic.entity.BusRouteLinkEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusRouteLinkDto {
    private Long busRouteLinkId;
    private Long dataListId;
    private String routeId;
    private String linkOrder;
    private String linkId;

    public BusRouteLinkEntity toEntity(){
        return BusRouteLinkEntity.builder()
                .busRouteLinkId(busRouteLinkId)
                .dataListId(dataListId)
                .routeId(routeId)
                .linkOrder(linkOrder)
                .linkId(linkId)
                .build();
    }
}
