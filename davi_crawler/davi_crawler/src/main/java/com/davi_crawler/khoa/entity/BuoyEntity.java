package com.davi_crawler.khoa.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.khoa.dto.BuoyDto;
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
        name = "BUOY_SEQ",
        sequenceName = ConstantUtil.BUOY_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name="DV_KHOA_BUOY")
@TypeDef(name="jsonb", typeClass = JsonBinaryType.class)
public class BuoyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUOY_SEQ")
    private Long buoyId;
    private Long dataListId;
    private String blfrNo;
    private String buoyKr;
    private String buoyEn;
    private String buoyNm;
    private String seaNm;
    private String lgtProperty;
    private String remark;
    @Type(type = "jsonb")
    private String lngLat;

    public BuoyDto toDto(){
        return BuoyDto.builder()
                .buoyId(buoyId)
                .dataListId(dataListId)
                .blfrNo(blfrNo)
                .buoyKr(buoyKr)
                .buoyEn(buoyEn)
                .buoyNm(buoyNm)
                .seaNm(seaNm)
                .lgtProperty(lgtProperty)
                .remark(remark)
                .lngLat(lngLat)
                .build();
    }
}
