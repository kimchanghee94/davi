package com.davi_server.khoa.dto;

import com.davi_server.common.dto.BasicDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeaDistDto extends BasicDto {
    private Long seaDistId;
    private Long dataListId;
    private String degreeLatArr;
    private String destHbrNm;
    private String num;
    private String longDistYn;
    private String hbrNmArr;
    private String dprtHbrNm;
    private String cal;
    private String hbrLonArr;
    private String fromNum;
    private String domstYn;
    private String degreeLonArr;
    private String hbrLatArr;
    private String dist;
    private int order;
    private String domStrTypeNm;
    private String domStrNm;
    private String domEndNm;
}
