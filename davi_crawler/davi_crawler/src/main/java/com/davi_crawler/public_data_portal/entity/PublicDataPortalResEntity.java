package com.davi_crawler.public_data_portal.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.public_data_portal.dto.PublicDataPortalResDto;
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
        name = "PDP_RES_SEQ"
        , sequenceName = ConstantUtil.PDPRES_SEQ_NAME
        , initialValue = 1
        , allocationSize = 1
)
@Table(name="DV_PUBLIC_DATA_PORTAL_RES")
public class PublicDataPortalResEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PDP_RES_SEQ")
    private Long publicDataPortalResId;
    private Long publicDataPortalServiceId;
    private String pColumn;
    private String columnNmKr;
    private String columnNmEn;
    private String columnSize;
    private String columnDiv;
    private String sampleData;
    private String columnDesc;

    public PublicDataPortalResDto toDto(){
        return PublicDataPortalResDto.builder()
                .publicDataPortalResId(publicDataPortalResId)
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
