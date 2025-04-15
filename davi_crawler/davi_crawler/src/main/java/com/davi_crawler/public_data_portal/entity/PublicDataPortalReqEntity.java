package com.davi_crawler.public_data_portal.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.public_data_portal.dto.PublicDataPortalReqDto;
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
        name = "PDP_REQ_SEQ"
        , sequenceName = ConstantUtil.PDPREQ_SEQ_NAME
        , initialValue = 1
        , allocationSize = 1
)
@Table(name="DV_PUBLIC_DATA_PORTAL_REQ")
public class PublicDataPortalReqEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PDP_REQ_SEQ")
    private Long publicDataPortalReqId;
    private Long publicDataPortalServiceId;
    private String pColumn;
    private String columnNmKr;
    private String columnNmEn;
    private String columnSize;
    private String columnDiv;
    private String sampleData;
    private String columnDesc;

    public PublicDataPortalReqDto toDto(){
        return PublicDataPortalReqDto.builder()
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
