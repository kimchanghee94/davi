package com.davi_crawler.molit.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.molit.dto.LiDto;
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
        name = "LI_SEQ",
        sequenceName = ConstantUtil.LI_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name="DV_MOLIT_LI")
@TypeDef(name="jsonb", typeClass = JsonBinaryType.class)
public class LiEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LI_SEQ")
    private Long liId;
    private Long dataListId;
    private String liEngNm;
    private String liKorNm;
    private String fullNm;
    private String liCd;
    @Type(type = "jsonb")
    private String jsonData;

    public LiDto toDto(){
        return LiDto.builder()
                .liId(liId)
                .dataListId(dataListId)
                .liEngNm(liEngNm)
                .liKorNm(liKorNm)
                .fullNm(fullNm)
                .liCd(liCd)
                .jsonData(jsonData)
                .build();
    }
}
