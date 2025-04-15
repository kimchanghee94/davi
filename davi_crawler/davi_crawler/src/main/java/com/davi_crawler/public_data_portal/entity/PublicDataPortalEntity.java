package com.davi_crawler.public_data_portal.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.public_data_portal.dto.PublicDataPortalDto;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
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
        name = "PDP_SEQ"
        , sequenceName = ConstantUtil.PDP_SEQ_NAME
        , initialValue = 1
        , allocationSize = 1
)
@Table(name="DV_PUBLIC_DATA_PORTAL")
public class PublicDataPortalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PDP_SEQ")
    private Long publicDataPortalId;
    private String apiName;
    private String taxonomy;
    private String provOrg;
    private String execDept;
    private String execDeptTelno;
    private String apiType;
    private String dataFormat;
    private String usesAppl;
    private String keyword;
    private String regiDate;
    private String editDate;
    private String chargeYn;
    private String availTraffic;
    private String reviewType;
    private String apiSiteUrl;
    private String apiServiceUrl;
    private String url;
    private String tableJsonFlag;
    public PublicDataPortalDto toDto(){
        return PublicDataPortalDto.builder()
                .publicDataPortalId(publicDataPortalId)
                .apiName(apiName)
                .taxonomy(taxonomy)
                .provOrg(provOrg)
                .execDept(execDept)
                .execDeptTelno(execDeptTelno)
                .apiType(apiType)
                .dataFormat(dataFormat)
                .usesAppl(usesAppl)
                .keyword(keyword)
                .regiDate(regiDate)
                .editDate(editDate)
                .chargeYn(chargeYn)
                .availTraffic(availTraffic)
                .reviewType(reviewType)
                .apiSiteUrl(apiSiteUrl)
                .apiServiceUrl(apiServiceUrl)
                .url(url)
                .tableJsonFlag(tableJsonFlag)
                .build();
    }
}
