package com.example.sveltespringboot.dto;

import com.example.sveltespringboot.entity.APIReqEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class APIReqDto {
    private Long apiReqId;
    private Long apiServiceId;
    private String pColumn;
    private String columnNmKr;
    private String columnNmEn;
    private String columnSize;
    private String columnDiv;
    private String sampleData;
    private String columnDesc;

    public APIReqEntity toEntity(){
        APIReqEntity apiReqEntity = APIReqEntity.builder()
                .apiReqId(apiReqId)
                .apiServiceId(apiServiceId)
                .pColumn(pColumn)
                .columnNmKr(columnNmKr)
                .columnNmEn(columnNmEn)
                .columnSize(columnSize)
                .columnDiv(columnDiv)
                .sampleData(sampleData)
                .columnDesc(columnDesc)
                .build();
        return apiReqEntity;
    }
}
