package com.davi_crawler.molit.dto;

import com.davi_crawler.molit.entity.LinkEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkDto {
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

    public LinkEntity toEntity(){
        return LinkEntity.builder()
                .linkId(linkId)
                .dataListId(dataListId)
                .roadName(roadName)
                .maxSpd(maxSpd)
                .rdRankH(rdRankH)
                .rdTypeH(rdTypeH)
                .restW(restW)
                .restH(restH)
                .remark(remark)
                .propLinkId(propLinkId)
                .jsonData(jsonData)
                .build();
    }
}
