package com.davi_crawler.khoa.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.khoa.dto.LsmdContDto;
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
        name = "LSMD_CONT_SEQ",
        sequenceName = ConstantUtil.LSMDCONT_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name="DV_KHOA_LSMD_CONT")
@TypeDef(name="jsonb", typeClass = JsonBinaryType.class)
public class LsmdContEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LSMD_CONT_SEQ")
    private Long lsmdContId;
    private Long dataListId;
    private String mnum;
    private String alias;
    private String remark;
    private String ntfdate;
    private String sggOid;
    private String colAdmSectCd;
    private String objectid;
    @Type(type = "jsonb")
    private String shape;

    public LsmdContDto toDto(){
        return LsmdContDto.builder()
                .lsmdContId(lsmdContId)
                .dataListId(dataListId)
                .mnum(mnum)
                .alias(alias)
                .remark(remark)
                .ntfdate(ntfdate)
                .sggOid(sggOid)
                .colAdmSectCd(colAdmSectCd)
                .objectid(objectid)
                .shape(shape)
                .build();
    }
}
