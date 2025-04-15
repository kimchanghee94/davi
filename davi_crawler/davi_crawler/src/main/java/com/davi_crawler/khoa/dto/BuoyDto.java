package com.davi_crawler.khoa.dto;

import com.davi_crawler.khoa.entity.BuoyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuoyDto {
    private Long buoyId;
    private Long dataListId;
    private String blfrNo;
    private String buoyKr;
    private String buoyEn;
    private String buoyNm;
    private String seaNm;
    private String lgtProperty;
    private String remark;
    private String lngLat;

    public BuoyEntity toEntity(){
        return BuoyEntity.builder()
                .buoyId(buoyId)
                .dataListId(dataListId)
                .blfrNo(blfrNo)
                .buoyKr(buoyKr)
                .buoyEn(buoyEn)
                .buoyNm(buoyNm)
                .seaNm(seaNm)
                .lgtProperty(lgtProperty)
                .remark(remark)
                .lngLat(lngLat)
                .build();
    }
}
