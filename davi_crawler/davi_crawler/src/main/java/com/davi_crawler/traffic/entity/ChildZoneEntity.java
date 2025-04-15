package com.davi_crawler.traffic.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.traffic.dto.ChildZoneDto;
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
        name = "CHILD_ZONE_SEQ",
        sequenceName = ConstantUtil.CHILDZONE_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name="DV_TRAFFIC_CHILD_ZONE")
public class ChildZoneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHILD_ZONE_SEQ")
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

    public ChildZoneDto toDto(){
        return ChildZoneDto.builder()
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
