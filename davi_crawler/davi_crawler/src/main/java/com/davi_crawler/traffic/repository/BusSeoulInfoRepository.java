package com.davi_crawler.traffic.repository;

import com.davi_crawler.traffic.entity.BusSeoulInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusSeoulInfoRepository extends JpaRepository<BusSeoulInfoEntity, Long> {
    List<BusSeoulInfoEntity> findByNodeOrder(String nodeOrder) throws Exception;
}
