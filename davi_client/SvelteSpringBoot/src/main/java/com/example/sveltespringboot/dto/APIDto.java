package com.example.sveltespringboot.dto;

import com.example.sveltespringboot.entity.APIEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class APIDto {
    private Long apiId;
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

    public APIEntity toEntity(){
        APIEntity apiEntity = APIEntity.builder()
                .apiId(apiId)
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
        return apiEntity;
    }
}
