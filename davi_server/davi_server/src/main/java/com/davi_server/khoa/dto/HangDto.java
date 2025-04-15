package com.davi_server.khoa.dto;

import com.davi_server.common.dto.BasicDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HangDto extends BasicDto {
    private Long hangId;
    private Long dataListId;
    private String hangName;
    private String hangSubName;
    private String lngLat;
}
