package com.davi_crawler.common.repository;

import com.davi_crawler.common.entity.DataColListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataColListRepository extends JpaRepository<DataColListEntity, Long> {
    Long deleteByDataListId(Long dataListId) throws Exception;
    List<DataColListEntity> findByDataListIdOrderByOrder(Long dataListId) throws Exception;
}
