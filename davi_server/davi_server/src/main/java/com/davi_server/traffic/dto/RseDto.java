package com.davi_server.traffic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RseDto {
    private Long rseId;
    private Long dataListId;
    private String rseManageNo;
    private String rseLocIdentiCode;
    private String latitude;
    private String longitude;
}
