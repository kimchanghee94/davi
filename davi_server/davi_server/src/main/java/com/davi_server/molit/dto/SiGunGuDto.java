package com.davi_server.molit.dto;

import com.davi_server.common.dto.BasicDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiGunGuDto extends BasicDto {
    private Long siGunGuId;
    private Long dataListId;
    private String sigEngNm;
    private String sigKorNm;
    private String fullNm;
    private String sigCd;
    private String jsonData;
    private String centerLat;
    private String centerLng;
}
