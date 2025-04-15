package com.davi_crawler.traffic.repository;

import com.davi_crawler.traffic.entity.CrosswalkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrosswalkRepository extends JpaRepository<CrosswalkEntity, Long> {
}
