package com.davi_crawler.disaster.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.disaster.dto.ChimsuDto;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(
        name = "CHIMSU_SEQ",
        sequenceName = ConstantUtil.CHIMSU_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "DV_DISASTER_CHIMSU")
@TypeDef(name="jsonb", typeClass = JsonBinaryType.class)
public class ChimsuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHIMSU_SEQ")
    private Long chimsuId;
    private Long dataListId;
    private String objectId;
    private String guNam;
    private String fShim;
    private String fDisaNm;
    private String fAvrHgt;
    private String fArea;
    private String admCd;
    private String fSatYmd;
    private String fEndYmd;
    private String fSatTm;
    private String fEndTm;
    private String fRsnDtl;
    private String fZoneNm;
    private String invYr;
    private String fYr;
    private String type;
    @Type(type="jsonb")
    private String jsonData;

    public ChimsuDto toDto(){
        return ChimsuDto.builder()
                .chimsuId(chimsuId)
                .dataListId(dataListId)
                .objectId(objectId)
                .guNam(guNam)
                .fShim(fShim)
                .fDisaNm(fDisaNm)
                .fAvrHgt(fAvrHgt)
                .fArea(fArea)
                .admCd(admCd)
                .fSatYmd(fSatYmd)
                .fEndYmd(fEndYmd)
                .fSatTm(fSatTm)
                .fEndTm(fEndTm)
                .fRsnDtl(fRsnDtl)
                .fZoneNm(fZoneNm)
                .invYr(invYr)
                .fYr(fYr)
                .type(type)
                .jsonData(jsonData)
                .build();
    }
}
