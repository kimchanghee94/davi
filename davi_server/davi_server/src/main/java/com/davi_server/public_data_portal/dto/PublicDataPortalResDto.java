package com.davi_server.public_data_portal.dto;

import com.davi_server.common.dto.BasicDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicDataPortalResDto extends BasicDto {
    private Long publicDataPortalResId;
    private Long publicDataPortalServiceId;
    private String pColumn;
    private String columnNmKr;
    private String columnNmEn;
    private String columnSize;
    private String columnDiv;
    private String sampleData;
    private String columnDesc;
}
