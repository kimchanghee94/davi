package com.davi_crawler.traffic.entity;

import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.traffic.dto.FogDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.bcel.Const;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(
        name = "FOG_SEQ",
        sequenceName = ConstantUtil.FOG_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "DV_TRAFFIC_FOG")
public class FogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FOG_SEQ")
    private Long fogId;
    private Long dataListId;
    private String poiSn;
    private String poiTyCode;
    private String roadTy;
    private String roadNo;
    private String poiNm;
    @Column(name="begin_map_x_crdnt")
    private String beginMapXCrdnt;
    @Column(name="begin_map_y_crdnt")
    private String beginMapYCrdnt;
    @Column(name="end_map_x_crdnt")
    private String endMapXCrdnt;
    @Column(name="end_map_y_crdnt")
    private String endMapYCrdnt;
    private String beginAdres;
    private String endAdres;
    private String drcNm;
    private String sctnLt;
    private String linkId;
    private String remark;
    private String useAt;
    private String registerId;
    private String registDt;
    private String updusrId;
    private String updtDt;
    private String dstrctId;
    private String ledcd;
    @Column(name="center_map_y_crdnt")
    private String centerMapYCrdnt;
    @Column(name="center_map_x_crdnt")
    private String centerMapXCrdnt;

    public FogDto toDto(){
        return FogDto.builder()
                .fogId(fogId)
                .dataListId(dataListId)
                .poiSn(poiSn)
                .poiTyCode(poiTyCode)
                .roadTy(roadTy)
                .roadNo(roadNo)
                .poiNm(poiNm)
                .beginMapXCrdnt(beginMapXCrdnt)
                .beginMapYCrdnt(beginMapYCrdnt)
                .endMapXCrdnt(endMapXCrdnt)
                .endMapYCrdnt(endMapYCrdnt)
                .beginAdres(beginAdres)
                .endAdres(endAdres)
                .drcNm(drcNm)
                .sctnLt(sctnLt)
                .linkId(linkId)
                .remark(remark)
                .useAt(useAt)
                .registerId(registerId)
                .registDt(registDt)
                .updusrId(updusrId)
                .updtDt(updtDt)
                .dstrctId(dstrctId)
                .ledcd(ledcd)
                .centerMapYCrdnt(centerMapYCrdnt)
                .centerMapXCrdnt(centerMapXCrdnt)
                .build();
    }
}
