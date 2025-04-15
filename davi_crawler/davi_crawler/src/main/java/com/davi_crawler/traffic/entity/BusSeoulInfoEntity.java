package com.davi_crawler.traffic.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.traffic.dto.BusSeoulInfoDto;
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
        name = "BUS_SEOUL_INFO_SEQ",
        sequenceName = ConstantUtil.BUSSEOULINFO_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name="DV_TRAFFIC_BUS_SEOUL_INFO")
public class BusSeoulInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUS_SEOUL_INFO_SEQ")
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

    public BusSeoulInfoDto toDto(){
        return BusSeoulInfoDto.builder()
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
