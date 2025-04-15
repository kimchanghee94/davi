package com.davi_server.public_data_portal.dto;

import com.davi_server.common.dto.BasicDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicDataPortalServiceDto extends BasicDto {
    private Long publicDataPortalServiceId;
    private Long publicDataPortalId;
    private String route;
    private int order;
    private String description;
}
