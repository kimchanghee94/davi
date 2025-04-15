package com.davi_crawler.common.service;

import com.davi_crawler.common.dto.DataColListDto;
import com.davi_crawler.common.dto.TableInfoDto;
import com.davi_crawler.common.entity.DataColListEntity;
import com.davi_crawler.common.repository.DataColListRepository;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class DataColListService {
    private final DataColListRepository dataColListRepository;
    private final TableInfoService tableInfoService;


    @Transactional(rollbackFor = {Exception.class})
    public void updateColData(Long dataListId, String tableNm) throws Exception{
        //테이블 정보 뽑아오기
        List<TableInfoDto> tableInfoDtos = tableInfoService.getTableInfo(tableNm);
        List<DataColListDto> orgTableInfos = selectByDataListId(dataListId);

        //기존과 변경사항 없으면 그대로 종료
        if(tableInfoDtos.size() == orgTableInfos.size() + 2){
            boolean isSame = true;

            for(int i=0; i<orgTableInfos.size(); i++){
                TableInfoDto tableInfoDto = tableInfoDtos.get(i + 2);
                DataColListDto dataColListDto = orgTableInfos.get(i);

                if((!tableInfoDto.getColumnName().equals(dataColListDto.getColName()))
                        || (!tableInfoDto.getColumnComment().equals(dataColListDto.getColDescript()))
                        || ((i + 1) != dataColListDto.getOrder())){
                    isSame = false;
                    break;
                }
            }

            //모두 동일한 경우
            if(isSame == true){
                return;
            }
        }

        List<DataColListDto> dataColListDtos = new ArrayList<>();

        for(int i=2; i < tableInfoDtos.size(); i++){
            TableInfoDto tableInfoDto = tableInfoDtos.get(i);

            DataColListDto dataColListDto = new DataColListDto();
            dataColListDto.setDataListId(dataListId);
            dataColListDto.setColName(tableInfoDto.getColumnName());
            dataColListDto.setColDescript(tableInfoDto.getColumnComment());
            dataColListDto.setOrder(Long.valueOf(i - 1));
            dataColListDtos.add(dataColListDto);
        }

        //특정 데이터 리스트만 삭제
        deleteByDataListId(dataListId);
        insertAll(dataColListDtos);
    }

    public List<DataColListDto> selectByDataListId(Long dataListId) throws Exception{
        List<DataColListEntity> dataColListEntities = dataColListRepository.findByDataListIdOrderByOrder(dataListId);
        return ConvertEntityDtoListUtil.toListDataColListDto(dataColListEntities);
    }

    public void insert(DataColListDto dataColListDto) throws Exception{
        DataColListEntity dataColListEntity = dataColListDto.toEntity();
        dataColListRepository.save(dataColListEntity);
    }

    public void insertAll(List<DataColListDto> dataColListDtos) throws Exception{
        List<DataColListEntity> dataColListEntities = ConvertEntityDtoListUtil.toListDataColListEntity(dataColListDtos);
        dataColListRepository.saveAll(dataColListEntities);
    }

    public void deleteByDataListId(Long dataListId) throws Exception{
        dataColListRepository.deleteByDataListId(dataListId);
    }
}