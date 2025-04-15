package com.example.sveltespringboot.entity;

import com.example.sveltespringboot.dto.APIDto;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@ToString
@Table(name="DV_API")
public class APIEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="API_ID")
    private Long apiId;
    @Column(name="API_NAME")
    private String apiName;
    private String taxonomy;
    @Column(name="PROV_ORG")
    private String provOrg;
    @Column(name="EXEC_DEPT")
    private String execDept;
    @Column(name="EXEC_DEPT_TELNO")
    private String execDeptTelno;
    @Column(name="API_TYPE")
    private String apiType;
    @Column(name="DATA_FORMAT")
    private String dataFormat;
    @Column(name="USES_APPL")
    private String usesAppl;
    private String keyword;
    @Column(name="REGI_DATE")
    private String regiDate;
    @Column(name="EDIT_DATE")
    private String editDate;
    @Column(name="CHARGE_YN")
    private String chargeYn;
    @Column(name="AVAIL_TRAFFIC")
    private String availTraffic;
    @Column(name="REVIEW_TYPE")
    private String reviewType;
    @Column(name="API_SITE_URL")
    private String apiSiteUrl;
    @Column(name="API_SERVICE_URL")
    private String apiServiceUrl;
    private String url;
    @Column(name="TABLE_JSON_FLAG")
    private String tableJsonFlag;

    public APIDto toDto(){
        APIDto apiDto = APIDto.builder()
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
        return apiDto;
    }
}


