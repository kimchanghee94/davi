package com.davi_crawler.khoa.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.khoa.dto.SeaDistDto;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(
        name = "SEA_DIST_SEQ",
        sequenceName = ConstantUtil.SEADIST_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name="DV_KHOA_SEA_DIST")
public class SeaDistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEA_DIST_SEQ")
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
    private String domStrTypeNm;
    private String domStrNm;
    private String domEndNm;
    private String psnSharing;

    @Column(name="\"order\"")
    private int order;
    public SeaDistDto toDto(){
        return SeaDistDto.builder()
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
