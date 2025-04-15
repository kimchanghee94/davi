package com.davi_crawler.khoa.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.khoa.dto.HangDto;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.geo.Point;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(
        name = "HANG_SEQ",
        sequenceName = ConstantUtil.HANG_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name="DV_KHOA_HANG")
@TypeDef(name="jsonb", typeClass = JsonBinaryType.class)
public class HangEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HANG_SEQ")
    private Long hangId;
    private Long dataListId;
    private String hangName;
    private String hangSubName;
    @Type(type="jsonb")
    private String lngLat;

    public HangDto toDto(){
        return HangDto.builder()
                .hangId(hangId)
                .dataListId(dataListId)
                .hangName(hangName)
                .hangSubName(hangSubName)
                .lngLat(lngLat)
                .build();
    }
}
