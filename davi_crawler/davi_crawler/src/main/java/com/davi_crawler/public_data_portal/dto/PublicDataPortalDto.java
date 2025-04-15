package com.davi_crawler.public_data_portal.dto;

import com.davi_crawler.public_data_portal.entity.PublicDataPortalEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicDataPortalDto {
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

    public PublicDataPortalEntity toEntity(){
        return PublicDataPortalEntity.builder()
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
