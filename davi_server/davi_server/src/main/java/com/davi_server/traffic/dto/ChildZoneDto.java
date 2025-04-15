package com.davi_server.traffic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
}
