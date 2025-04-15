package com.davi_crawler.molit.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.molit.dto.LinkDto;
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
        name = "LINK_SEQ",
        sequenceName = ConstantUtil.LINK_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name="DV_MOLIT_LINK")
@TypeDef(name="jsonb", typeClass = JsonBinaryType.class)
public class LinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LINK_SEQ")
    private Long linkId;
    private Long dataListId;
    private String roadName;
    private String maxSpd;
    @Column(name="rd_rank_h")
    private String rdRankH;
    @Column(name="rd_type_h")
    private String rdTypeH;
    @Column(name="rest_w")
    private String restW;
    @Column(name="rest_h")
    private String restH;
    private String remark;
    private String propLinkId;
    @Type(type = "jsonb")
    private String jsonData;

    public LinkDto toDto(){
        return LinkDto.builder()
                .linkId(linkId)
                .dataListId(dataListId)
                .roadName(roadName)
                .maxSpd(maxSpd)
                .rdRankH(rdRankH)
                .rdTypeH(rdTypeH)
                .restW(restW)
                .restH(restH)
                .remark(remark)
                .propLinkId(propLinkId)
                .jsonData(jsonData)
                .build();
    }
}
