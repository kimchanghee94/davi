package com.davi_crawler.traffic.repository;

import com.davi_crawler.traffic.entity.TrafficLightEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrafficLightRepository extends JpaRepository<TrafficLightEntity, Long> {
}
