package com.davi_crawler.traffic.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.traffic.dto.RseDto;
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
        name = "RSE_SEQ",
        sequenceName = ConstantUtil.RSE_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name="DV_TRAFFIC_RSE")
public class RseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RSE_SEQ")
    private Long rseId;
    private Long dataListId;
    private String rseManageNo;
    private String rseLocIdentiCode;
    private String latitude;
    private String longitude;

    public RseDto toDto(){
        return RseDto.builder()
                .rseId(rseId)
                .dataListId(dataListId)
                .rseManageNo(rseManageNo)
                .rseLocIdentiCode(rseLocIdentiCode)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
