package com.davi_crawler.traffic.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.traffic.dto.BusRouteLinkDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(
        name = "BUS_ROUTE_LINK_SEQ",
        sequenceName = ConstantUtil.BUSROUTELINK_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name="DV_TRAFFIC_BUS_ROUTE_LINK")
public class BusRouteLinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUS_ROUTE_LINK_SEQ")
    private Long busRouteLinkId;
    private Long dataListId;
    private String routeId;
    private String linkOrder;
    private String linkId;

    public BusRouteLinkDto toDto(){
        return BusRouteLinkDto.builder()
                .busRouteLinkId(busRouteLinkId)
                .dataListId(dataListId)
                .routeId(routeId)
                .linkOrder(linkOrder)
                .linkId(linkId)
                .build();
    }
}
