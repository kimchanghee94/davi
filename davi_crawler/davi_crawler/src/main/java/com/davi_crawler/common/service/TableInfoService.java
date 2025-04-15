package com.davi_crawler.common.service;

import com.davi_crawler.common.dto.TableInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class TableInfoService {
    private final EntityManager entityManager;

    public List<TableInfoDto> getTableInfo(String tableNm) throws Exception{
        String queryStr = "SELECT" +
                "    a.attname AS column_name," +
                "    d.description AS column_comment " +
                "FROM" +
                "    pg_attribute a " +
                "LEFT JOIN" +
                "    pg_description d ON a.attrelid = d.objoid AND a.attnum = d.objsubid " +
                "WHERE" +
                "    a.attrelid = 'public.dv_disaster_chimsu' :::: regclass" +
                "    AND a.attname NOT IN ('tableoid', 'cmax', 'xmax', 'cmin', 'xmin', 'ctid')";

        List<Object[]> result = entityManager.createNativeQuery(queryStr).getResultList();
        List<TableInfoDto> tableInfoDtos = new ArrayList<>();

        for(Object[] row : result){
            TableInfoDto tableInfoDto = new TableInfoDto();
            tableInfoDto.setColumnName((String) row[0]);

            //주석이 없는 경우 칼럼명으로 채워넣는다
            if((String) row[1] == null){
                tableInfoDto.setColumnComment((String) row[0]);
            }else{
                tableInfoDto.setColumnComment((String) row[1]);
            }

            tableInfoDtos.add(tableInfoDto);
        }

        return tableInfoDtos;
    }
}
