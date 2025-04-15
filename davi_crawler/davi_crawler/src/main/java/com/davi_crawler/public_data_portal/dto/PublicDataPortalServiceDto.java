package com.davi_crawler.public_data_portal.dto;

import com.davi_crawler.public_data_portal.entity.PublicDataPortalServiceEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicDataPortalServiceDto {
    private Long publicDataPortalServiceId;
    private Long publicDataPortalId;
    private String route;
    private int order;
    private String description;

    public PublicDataPortalServiceEntity toEntity(){
        return PublicDataPortalServiceEntity.builder()
                .publicDataPortalServiceId(publicDataPortalServiceId)
                .publicDataPortalId(publicDataPortalId)
                .route(route)
                .order(order)
                .description(description)
                .build();
    }
}
