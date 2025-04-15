package com.davi_crawler.common.service;

import com.davi_crawler.common.dto.DataListDto;
import com.davi_crawler.common.entity.DataListEntity;
import com.davi_crawler.common.repository.DataListRepository;
import com.davi_crawler.common.util.ApiUtil;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class DataListService {
    private final DataListRepository dataListRepository;
    private final CacheManager cacheManager;

    @Transactional(rollbackFor = {Exception.class})
    public void insert(DataListDto dataListDto) throws Exception{
        log.info(dataListDto);
        dataListRepository.save(dataListDto.toEntity());
    }

    //프로그램이 시작되고 crawler 테이블에 대해 최초로 긁어와 cache에 등록해둔다.
    @EventListener(ApplicationReadyEvent.class)
    public void initSelectAll() throws Exception{
        List<DataListEntity> dataListEntityList = dataListRepository.findAll();
        cacheManager.getCache("dataList").put(ConstantUtil.DATA_LIST_INIT_CACHE_KEY, dataListEntityList);
        log.debug("INIT : " + dataListEntityList.toString());
    }

    @Cacheable(value="dataList", key="#key")
    public List<DataListDto> selectAll(String key) throws Exception{
        List<DataListEntity> dataListEntityList = dataListRepository.findAll();
        List<DataListDto> dataListDtoList = ConvertEntityDtoListUtil.toListDataListDto(dataListEntityList);
        log.debug("SELECT : " + dataListDtoList);
        return dataListDtoList;
    }

    @CachePut(value="dataList", key="#key")
    public List<DataListDto> reSelectAll(String key) throws Exception{
        List<DataListEntity> dataListEntityList = dataListRepository.findAll();
        List<DataListDto> dataListDtoList = ConvertEntityDtoListUtil.toListDataListDto(dataListEntityList);
        log.debug("CACHE UPDATE" + dataListDtoList);
        return dataListDtoList;
    }

    public DataListDto selectById(Long id) throws Exception{
        return dataListRepository.findById(id).get().toDto();
    }
}
