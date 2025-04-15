package com.davi_crawler.traffic.repository;

import com.davi_crawler.traffic.entity.CctvEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CctvRepository extends JpaRepository<CctvEntity, Long> {
}
