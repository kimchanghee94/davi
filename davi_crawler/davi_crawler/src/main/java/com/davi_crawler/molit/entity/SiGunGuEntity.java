package com.davi_crawler.molit.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.molit.dto.SiDoDto;
import com.davi_crawler.molit.dto.SiGunGuDto;
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
        name = "SI_GUN_GU_SEQ",
        sequenceName = ConstantUtil.SIGUNGU_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name="DV_MOLIT_SI_GUN_GU")
@TypeDef(name="jsonb", typeClass = JsonBinaryType.class)
public class SiGunGuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SI_GUN_GU_SEQ")
    private Long siGunGuId;
    private Long dataListId;
    private String sigEngNm;
    private String sigKorNm;
    private String fullNm;
    private String sigCd;
    @Type(type = "jsonb")
    private String jsonData;
    private String centerLat;
    private String centerLng;

    public SiGunGuDto toDto(){
        return SiGunGuDto.builder()
                .siGunGuId(siGunGuId)
                .dataListId(dataListId)
                .sigEngNm(sigEngNm)
                .sigKorNm(sigKorNm)
                .fullNm(fullNm)
                .sigCd(sigCd)
                .jsonData(jsonData)
                .centerLat(centerLat)
                .centerLng(centerLng)
                .build();
    }
}
