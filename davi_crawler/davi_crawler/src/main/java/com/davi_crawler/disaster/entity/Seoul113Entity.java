package com.davi_crawler.disaster.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.disaster.dto.Seoul113Dto;
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
        name = "SEOUL113_SEQ",
        sequenceName = ConstantUtil.SEOUL113_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name="DV_DISASTER_SEOUL113")
@TypeDef(name="jsonb", typeClass = JsonBinaryType.class)
public class Seoul113Entity {
    @Id
    @Column(name="seoul113_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEOUL113_SEQ")
    private Long seoul113Id;
    private Long dataListId;
    private String category;
    private String areaCd;
    private String areaNm;
    @Type(type = "jsonb")
    private String jsonData;

    public Seoul113Dto toDto(){
        return Seoul113Dto.builder()
                .seoul113Id(seoul113Id)
                .dataListId(dataListId)
                .category(category)
                .areaCd(areaCd)
                .areaNm(areaNm)
                .jsonData(jsonData)
                .build();
    }
}
