package com.example.sveltespringboot.dto;

import com.example.sveltespringboot.entity.APIResEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class APIResDto {
    private Long apiResId;
    private Long apiServiceId;
    private String pColumn;
    private String columnNmKr;
    private String columnNmEn;
    private String columnSize;
    private String columnDiv;
    private String sampleData;
    private String columnDesc;

    public APIResEntity toEntity(){
        APIResEntity apiResEntity = APIResEntity.builder()
                .apiResId(apiResId)
                .apiServiceId(apiServiceId)
                .pColumn(pColumn)
                .columnNmKr(columnNmKr)
                .columnNmEn(columnNmEn)
                .columnSize(columnSize)
                .columnDiv(columnDiv)
                .sampleData(sampleData)
                .columnDesc(columnDesc)
                .build();

        return apiResEntity;
    }
}
