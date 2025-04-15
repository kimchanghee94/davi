package com.davi_server.khoa.dto;

import com.davi_server.common.dto.BasicDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LsmdContDto extends BasicDto {
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
}
