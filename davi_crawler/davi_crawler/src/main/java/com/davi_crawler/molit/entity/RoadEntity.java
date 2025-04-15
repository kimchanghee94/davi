package com.davi_crawler.molit.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.molit.dto.RoadDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(
        name = "ROAD_SEQ",
        sequenceName = ConstantUtil.ROAD_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name="DV_MOLIT_ROAD")
public class RoadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROAD_SEQ")
    private Long roadId;
    private Long dataListId;
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
    private String sigCd;

    public RoadDto toDto(){
        return RoadDto.builder()
                .roadId(roadId)
                .dataListId(dataListId)
                .rdsManNo(rdsManNo)
                .rn(rn)
                .rnCd(rnCd)
                .engRn(engRn)
                .ntfcDe(ntfcDe)
                .wdrRdCd(wdrRdCd)
                .roaClsSe(roaClsSe)
                .rdsDpnSe(rdsDpnSe)
                .rbpCn(rbpCn)
                .repCn(repCn)
                .roadBt(roadBt)
                .roadLt(roadLt)
                .bsiInt(bsiInt)
                .alwncResn(alwncResn)
                .alwncDe(alwncDe)
                .mvmResCd(mvmResCd)
                .mvmnResn(mvmnResn)
                .mvmnDe(mvmnDe)
                .opertDe(opertDe)
                .sigCd(sigCd)
                .build();
    }
}
