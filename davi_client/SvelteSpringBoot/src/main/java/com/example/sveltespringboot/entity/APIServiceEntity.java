package com.example.sveltespringboot.entity;

import com.example.sveltespringboot.dto.APIDto;
import com.example.sveltespringboot.dto.APIServiceDto;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name="DV_API_SERVICE")
public class APIServiceEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="API_SERVICE_ID")
    private Long apiServiceId;
    @Column(name="API_ID")
    private Long apiId;
    private String route;
    @Column(name="\"order\"")
    private int order;
    private String description;

    public APIServiceDto toDto(){
        APIServiceDto apiServiceDto = APIServiceDto.builder()
                .apiServiceId(apiServiceId)
                .apiId(apiId)
                .route(route)
                .order(order)
                .description(description)
                .build();
        return apiServiceDto;
    }
}
