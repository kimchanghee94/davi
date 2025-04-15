package com.example.sveltespringboot.dto;

import com.example.sveltespringboot.entity.APIColumnListEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class APIColumnListDto {
    private Long columnListId;
    private APIDto apiDto;
    private String columnNmKr;
    private String columnNmEn;
    private String columnSize;
    private String columnDiv;
    private String sampleData;
    private String comment;

    public APIColumnListEntity toEntity(){
        APIColumnListEntity apiColumnListEntity = APIColumnListEntity.builder()
                .columnListId(columnListId)
                .apiEntity(apiDto.toEntity())
                .columnNmKr(columnNmKr)
                .columnNmEn(columnNmEn)
                .columnSize(columnSize)
                .columnDiv(columnDiv)
                .sampleData(sampleData)
                .comment(comment)
                .build();
        return apiColumnListEntity;
    }
}
