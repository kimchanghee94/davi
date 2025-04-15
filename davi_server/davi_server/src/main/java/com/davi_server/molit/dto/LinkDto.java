package com.davi_server.molit.dto;

import com.davi_server.common.dto.BasicDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkDto extends BasicDto {
    private Long linkId;
    private Long dataListId;
    private String roadName;
    private String maxSpd;
    private String rdRankH;
    private String rdTypeH;
    private String restW;
    private String restH;
    private String remark;
    private String propLinkId;
    private String jsonData;
    private String paramSpeed;
    private String paramTrfCgt;
}
