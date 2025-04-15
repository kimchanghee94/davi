package com.davi_crawler.common.repository;

import com.davi_crawler.common.entity.DataListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataListRepository extends JpaRepository<DataListEntity, Long> {
}
