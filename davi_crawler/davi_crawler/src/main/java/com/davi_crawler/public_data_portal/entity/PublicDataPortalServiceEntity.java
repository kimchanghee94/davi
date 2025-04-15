package com.davi_crawler.public_data_portal.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.public_data_portal.dto.PublicDataPortalServiceDto;
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
        name = "PDP_SERVICE_SEQ_NAME"
        , sequenceName = ConstantUtil.PDPSERVICE_SEQ_NAME
        , initialValue = 1
        , allocationSize = 1
)
@Table(name="DV_PUBLIC_DATA_PORTAL_SERVICE")
public class PublicDataPortalServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PDP_SERVICE_SEQ_NAME")
    private Long publicDataPortalServiceId;
    private Long publicDataPortalId;
    private String route;

    @Column(name="\"order\"")
    private int order;
    private String description;

    public PublicDataPortalServiceDto toDto(){
        return PublicDataPortalServiceDto.builder()
                .publicDataPortalServiceId(publicDataPortalServiceId)
                .publicDataPortalId(publicDataPortalId)
                .route(route)
                .order(order)
                .description(description)
                .build();
    }
}
