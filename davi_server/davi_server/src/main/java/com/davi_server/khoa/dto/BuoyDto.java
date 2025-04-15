package com.davi_server.khoa.dto;

import com.davi_server.common.dto.BasicDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuoyDto extends BasicDto {
    private Long buoyId;
    private Long dataListId;
    private String blfrNo;
    private String buoyKr;
    private String buoyEn;
    private String buoyNm;
    private String seaNm;
    private String lgtProperty;
    private String remark;
    private String lngLat;
}
