package com.davi_server.molit.dto;

import com.davi_server.common.dto.BasicDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiDto extends BasicDto {
    private Long liId;
    private Long dataListId;
    private String liEngNm;
    private String liKorNm;
    private String fullNm;
    private String liCd;
    private String jsonData;
}
