package com.davi_crawler.disaster.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.disaster.dto.CivilDefenseDto;
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
        name = "CIVIL_DEFENSE_SEQ",
        sequenceName = ConstantUtil.CIVILDEFENSE_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "DV_DISASTER_CIVIL_DEFENSE")
public class CivilDefenseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CIVIL_DEFENSE_SEQ")
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

    public CivilDefenseDto toDto(){
        return CivilDefenseDto.builder()
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
