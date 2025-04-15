package com.davi_crawler.common.entity;

import com.davi_crawler.common.dto.DataColListDto;
import com.davi_crawler.common.util.ConstantUtil;
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
        name = "DATA_COL_LIST_SEQ",
        sequenceName = ConstantUtil.DATACOLLIST_SEQ_NAME,
        initialValue = 1,
        allocationSize = 1
)
@Table(name="DV_DATA_COL_LIST")
public class DataColListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DATA_COL_LIST_SEQ")
    private Long colListId;
    private Long dataListId;
    private String colName;
    private String colDescript;
    @Column(name="\"order\"")
    private Long order;

    public DataColListDto toDto(){
        return DataColListDto.builder()
                .colListId(colListId)
                .dataListId(dataListId)
                .colName(colName)
                .colDescript(colDescript)
                .order(order)
                .build();
    }
}
