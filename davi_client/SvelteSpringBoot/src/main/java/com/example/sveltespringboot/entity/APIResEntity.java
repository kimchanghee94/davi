package com.example.sveltespringboot.entity;

import com.example.sveltespringboot.dto.APIResDto;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name="DV_API_RES")
public class APIResEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="API_RES_ID")
    private Long apiResId;
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

    public APIResDto toDto(){
        APIResDto apiResDto = APIResDto.builder()
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

        return apiResDto;
    }
}
