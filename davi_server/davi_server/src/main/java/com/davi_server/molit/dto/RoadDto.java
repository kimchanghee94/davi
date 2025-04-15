package com.davi_server.molit.dto;

import com.davi_server.common.dto.BasicDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoadDto extends BasicDto {
    private Long roadId;
    private Long dataListId;
    private String sigCd;
    private String rdsManNo;
    private String rn;
    private String rnCd;
    private String engRn;
    private String ntfcDe;
    private String wdrRdCd;
    private String roaClsSe;
    private String rdsDpnSe;
    private String rbpCn;
    private String repCn;
    private String roadBt;
    private String roadLt;
    private String bsiInt;
    private String alwncResn;
    private String alwncDe;
    private String mvmResCd;
    private String mvmnResn;
    private String mvmnDe;
    private String opertDe;
}
