package com.davi_crawler.traffic.repository;

import com.davi_crawler.traffic.entity.SpeedDumpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeedDumpRepository extends JpaRepository<SpeedDumpEntity, Long> {
}
