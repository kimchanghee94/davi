package com.davi_crawler.molit.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.molit.dto.EupMyeonDongDto;
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
        name = "EUP_MYEON_DONG_SEQ",
        sequenceName = ConstantUtil.EUPMYEONDONG_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name="DV_MOLIT_EUP_MYEON_DONG")
@TypeDef(name="jsonb", typeClass = JsonBinaryType.class)
public class EupMyeonDongEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EUP_MYEON_DONG_SEQ")
    private Long eupMyeonDongId;
    private Long dataListId;
    private String emdEngNm;
    private String emdKorNm;
    private String fullNm;
    private String emdCd;
    @Type(type = "jsonb")
    private String jsonData;
    private String hdongCd;
    private String bdongCd;
    private String centerLat;
    private String centerLng;

    public EupMyeonDongDto toDto(){
        return EupMyeonDongDto.builder()
                .eupMyeonDongId(eupMyeonDongId)
                .dataListId(dataListId)
                .emdEngNm(emdEngNm)
                .emdKorNm(emdKorNm)
                .fullNm(fullNm)
                .emdCd(emdCd)
                .jsonData(jsonData)
                .hdongCd(hdongCd)
                .bdongCd(bdongCd)
                .centerLat(centerLat)
                .centerLng(centerLng)
                .build();
    }
}
