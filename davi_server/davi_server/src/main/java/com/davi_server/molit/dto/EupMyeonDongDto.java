package com.davi_server.molit.dto;

import com.davi_server.common.dto.BasicDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EupMyeonDongDto extends BasicDto {
    private Long eupMyeonDongId;
    private Long dataListId;
    private String emdEngNm;
    private String emdKorNm;
    private String fullNm;
    private String emdCd;
    private String jsonData;
    private String hdongCd;
    private String bdongCd;
    private String centerLat;
    private String centerLng;
}
