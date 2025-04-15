package com.davi_server.public_data_portal.dto;

import com.davi_server.common.dto.BasicDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicDataPortalDto extends BasicDto {
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
}
