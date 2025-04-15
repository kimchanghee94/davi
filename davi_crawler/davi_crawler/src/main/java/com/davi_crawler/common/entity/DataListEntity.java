package com.davi_crawler.common.entity;

import com.davi_crawler.common.dto.DataListDto;
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
        name = "DATA_LIST_SEQ"
        , sequenceName = "DV_DATA_LIST_DATA_LIST_ID_SEQ"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name="DV_DATA_LIST")
public class DataListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DATA_LIST_SEQ")
    private Long dataListId;
    private String name;
    private String url;
    private String tableNm;
    private Integer metaType;
    private String period;
    private String lastUpdate;

    public DataListDto toDto(){
        return DataListDto.builder()
                .dataListId(dataListId)
                .name(name)
                .url(url)
                .tableNm(tableNm)
                .metaType(metaType)
                .period(period)
                .lastUpdate(lastUpdate)
                .build();
    }
}
