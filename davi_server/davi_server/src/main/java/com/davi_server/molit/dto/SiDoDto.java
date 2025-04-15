package com.davi_server.molit.dto;

import com.davi_server.common.dto.BasicDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiDoDto extends BasicDto {
    private Long siDoId;
    private Long dataListId;
    private String ctpEngNm;
    private String ctpKorNm;
    private String ctprvnCd;
    private String jsonData;
}
