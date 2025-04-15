package com.davi_crawler.molit.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.molit.dto.SiDoDto;
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
        name = "SI_DO_SEQ",
        sequenceName = ConstantUtil.SIDO_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name="DV_MOLIT_SI_DO")
@TypeDef(name="jsonb", typeClass = JsonBinaryType.class)
public class SiDoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SI_DO_SEQ")
    private Long siDoId;
    private Long dataListId;
    private String ctpEngNm;
    private String ctpKorNm;
    private String ctprvnCd;
    @Type(type = "jsonb")
    private String jsonData;

    public SiDoDto toDto(){
        return SiDoDto.builder()
                .siDoId(siDoId)
                .dataListId(dataListId)
                .ctpEngNm(ctpEngNm)
                .ctpKorNm(ctpKorNm)
                .ctprvnCd(ctprvnCd)
                .jsonData(jsonData)
                .build();
    }
}
