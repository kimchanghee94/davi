package com.example.sveltespringboot.dto;

import com.example.sveltespringboot.entity.APIServiceEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class APIServiceDto {
    private Long apiServiceId;
    private Long apiId;
    private String route;
    private int order;
    private String description;
    public APIServiceEntity toEntity(){
        APIServiceEntity apiServiceEntity = APIServiceEntity.builder()
                .apiServiceId(apiServiceId)
                .apiId(apiId)
                .route(route)
                .order(order)
                .description(description)
                .build();
        return apiServiceEntity;
    }
}
