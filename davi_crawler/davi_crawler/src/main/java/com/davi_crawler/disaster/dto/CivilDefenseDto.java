package com.davi_crawler.disaster.dto;

import com.davi_crawler.disaster.entity.CivilDefenseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CivilDefenseDto {
    private Long civilDefenseId;
    private Long dataListId;
    private String manageNo;
    private String registerDt;
    private String operStatus;
    private String facilityNm;
    private String facilityClass;
    private String roadFullNm;
    private String locFullNm;
    private String zipCode;
    private String aboveUnder;
    private String area;
    private String maxCapacity;
    private String latitude;
    private String longitude;

    public CivilDefenseEntity toEntity(){
        return CivilDefenseEntity.builder()
                .civilDefenseId(civilDefenseId)
                .dataListId(dataListId)
                .manageNo(manageNo)
                .registerDt(registerDt)
                .operStatus(operStatus)
                .facilityNm(facilityNm)
                .facilityClass(facilityClass)
                .roadFullNm(roadFullNm)
                .locFullNm(locFullNm)
                .zipCode(zipCode)
                .aboveUnder(aboveUnder)
                .area(area)
                .maxCapacity(maxCapacity)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
