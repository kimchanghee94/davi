package com.davi_crawler.khoa.dto;

import com.davi_crawler.khoa.entity.LsmdContEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LsmdContDto {
    private Long lsmdContId;
    private Long dataListId;
    private String mnum;
    private String alias;
    private String remark;
    private String ntfdate;
    private String sggOid;
    private String colAdmSectCd;
    private String objectid;
    private String shape;

    public LsmdContEntity toEntity(){
        return LsmdContEntity.builder()
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
