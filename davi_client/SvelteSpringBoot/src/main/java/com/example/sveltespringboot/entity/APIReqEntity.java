package com.example.sveltespringboot.entity;

import com.example.sveltespringboot.dto.APIReqDto;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name="DV_API_REQ")
public class APIReqEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="API_REQ_ID")
    private Long apiReqId;
    @Column(name="API_SERVICE_ID")
    private Long apiServiceId;
    @Column(name="P_COLUMN")
    private String pColumn;
    @Column(name="COLUMN_NM_KR")
    private String columnNmKr;
    @Column(name="COLUMN_NM_EN")
    private String columnNmEn;
    @Column(name="COLUMN_SIZE")
    private String columnSize;
    @Column(name="COLUMN_DIV")
    private String columnDiv;
    @Column(name="SAMPLE_DATA")
    private String sampleData;
    @Column(name="COLUMN_DESC")
    private String columnDesc;

    public APIReqDto toDto(){
        APIReqDto apiReqDto = APIReqDto.builder()
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
        return apiReqDto;
    }
}
