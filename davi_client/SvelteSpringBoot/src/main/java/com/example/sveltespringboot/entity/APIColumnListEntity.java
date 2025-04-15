package com.example.sveltespringboot.entity;

import com.example.sveltespringboot.dto.APIColumnListDto;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="DV_API_KEYLIST")
public class APIColumnListEntity {
    @Id
    private Long columnListId;
    @OneToOne
    @JoinColumn(name="API_ID")
    private APIEntity apiEntity;
    private String columnNmKr;
    private String columnNmEn;
    private String columnSize;
    private String columnDiv;
    private String sampleData;
    private String comment;

    public APIColumnListDto toDto(){
        APIColumnListDto apiColumnListDto = APIColumnListDto.builder()
                .columnListId(columnListId)
                .apiDto(apiEntity.toDto())
                .columnNmKr(columnNmKr)
                .columnNmEn(columnNmEn)
                .columnSize(columnSize)
                .columnDiv(columnDiv)
                .sampleData(sampleData)
                .comment(comment)
                .build();
        return apiColumnListDto;
    }
}
