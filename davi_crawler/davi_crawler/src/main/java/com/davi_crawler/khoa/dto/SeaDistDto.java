package com.davi_crawler.khoa.dto;

import com.davi_crawler.khoa.entity.SeaDistEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeaDistDto {
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
    private String psnSharing;

    public SeaDistEntity toEntity(){
        return SeaDistEntity.builder()
                .seaDistId(seaDistId)
                .dataListId(dataListId)
                .degreeLatArr(degreeLatArr)
                .destHbrNm(destHbrNm)
                .num(num)
                .longDistYn(longDistYn)
                .hbrNmArr(hbrNmArr)
                .dprtHbrNm(dprtHbrNm)
                .cal(cal)
                .hbrLonArr(hbrLonArr)
                .fromNum(fromNum)
                .domstYn(domstYn)
                .degreeLonArr(degreeLonArr)
                .hbrLatArr(hbrLatArr)
                .dist(dist)
                .order(order)
                .domStrTypeNm(domStrTypeNm)
                .domStrNm(domStrNm)
                .domEndNm(domEndNm)
                .psnSharing(psnSharing)
                .build();
    }
}
