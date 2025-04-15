package com.davi_server.traffic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusRouteLinkDto {
    private Long busRouteLinkId;
    private Long dataListId;
    private String routeId;
    private String linkOrder;
    private String linkId;
}
