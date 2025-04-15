package com.davi_crawler.traffic.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.traffic.dto.BusRoutePathDto;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(
        name = "BUS_ROUTE_PATH_SEQ",
        sequenceName = ConstantUtil.BUSROUTEPATH_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "DV_TRAFFIC_BUS_ROUTE_PATH")
@TypeDef(name="jsonb", typeClass = JsonBinaryType.class)
public class BusRoutePathEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUS_ROUTE_PATH_SEQ")
    private Long busRoutePathId;
    private Long dataListId;
    private String routeId;
    @Type(type="jsonb")
    private String jsonData;

    public BusRoutePathDto toDto(){
        return BusRoutePathDto.builder()
                .busRoutePathId(busRoutePathId)
                .dataListId(dataListId)
                .routeId(routeId)
                .jsonData(jsonData)
                .build();
    }
}
