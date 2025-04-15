package com.davi_crawler.traffic.dto;

import com.davi_crawler.traffic.entity.RseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RseDto {
    private Long rseId;
    private Long dataListId;
    private String rseManageNo;
    private String rseLocIdentiCode;
    private String latitude;
    private String longitude;

    public RseEntity toEntity(){
        return RseEntity.builder()
                .rseId(rseId)
                .dataListId(dataListId)
                .rseManageNo(rseManageNo)
                .rseLocIdentiCode(rseLocIdentiCode)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
