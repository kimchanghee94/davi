package com.davi_crawler.traffic.dto;

import com.davi_crawler.traffic.entity.ChildZoneEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildZoneDto {
    private Long childZoneId;
    private Long dataListId;
    private String fcltyKnd;
    private String trgetFcltyNm;
    private String rdnmadr;
    private String lnmadr;
    private String latitude;
    private String longitude;
    private String institutionNm;
    private String cmptncPolcsttnNm;
    private String cctvYn;
    private String cctvNumber;
    private String prtcareaRw;
    private String referenceDate;
    private String insttCode;

    public ChildZoneEntity toEntity(){
        return ChildZoneEntity.builder()
                .childZoneId(childZoneId)
                .dataListId(dataListId)
                .fcltyKnd(fcltyKnd)
                .trgetFcltyNm(trgetFcltyNm)
                .rdnmadr(rdnmadr)
                .lnmadr(lnmadr)
                .latitude(latitude)
                .longitude(longitude)
                .institutionNm(institutionNm)
                .cmptncPolcsttnNm(cmptncPolcsttnNm)
                .cctvYn(cctvYn)
                .cctvNumber(cctvNumber)
                .prtcareaRw(prtcareaRw)
                .referenceDate(referenceDate)
                .insttCode(insttCode)
                .build();
    }
}
