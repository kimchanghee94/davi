package com.davi_server.common.dto;

import com.davi_server.common.dto.BasicDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinDto extends BasicDto {
    String routeName;
    List<String> selectColumn;
    List<String> joinColumn;
    List<HashMap<String, Object>> joinData;
}
