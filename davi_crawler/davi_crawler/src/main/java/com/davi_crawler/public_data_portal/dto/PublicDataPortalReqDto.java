package com.davi_crawler.public_data_portal.dto;

import com.davi_crawler.public_data_portal.entity.PublicDataPortalReqEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicDataPortalReqDto {
    private Long publicDataPortalReqId;
    private Long publicDataPortalServiceId;
    private String pColumn;
    private String columnNmKr;
    private String columnNmEn;
    private String columnSize;
    private String columnDiv;
    private String sampleData;
    private String columnDesc;

    public PublicDataPortalReqEntity toEntity(){
        return PublicDataPortalReqEntity.builder()
                .publicDataPortalReqId(publicDataPortalReqId)
                .publicDataPortalServiceId(publicDataPortalServiceId)
                .pColumn(pColumn)
                .columnNmKr(columnNmKr)
                .columnNmEn(columnNmEn)
                .columnSize(columnSize)
                .columnDiv(columnDiv)
                .sampleData(sampleData)
                .columnDesc(columnDesc)
                .build();
    }
}
