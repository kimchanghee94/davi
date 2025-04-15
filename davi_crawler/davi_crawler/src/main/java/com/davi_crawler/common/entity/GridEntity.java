package com.davi_crawler.common.entity;

import com.davi_crawler.common.dto.GridDto;
import com.davi_crawler.common.util.ConstantUtil;
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
        name = "GRID_SEQ",
        sequenceName = ConstantUtil.GRID_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name="DV_COMMON_GRID")
@TypeDef(name="jsonb", typeClass = JsonBinaryType.class)
public class GridEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GRID_SEQ")
    private Long gridId;
    private Long dataListId;
    private String gridLevel;
    private String centerLat;
    private String centerLng;
    @Type(type = "jsonb")
    private String jsonData;

    public GridDto toDto(){
        return GridDto.builder()
                .gridId(gridId)
                .dataListId(dataListId)
                .gridLevel(gridLevel)
                .centerLat(centerLat)
                .centerLng(centerLng)
                .jsonData(jsonData)
                .build();
    }
}
