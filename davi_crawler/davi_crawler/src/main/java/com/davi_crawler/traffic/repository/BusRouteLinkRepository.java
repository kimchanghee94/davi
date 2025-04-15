package com.davi_crawler.traffic.repository;

import com.davi_crawler.traffic.entity.BusRouteLinkEntity;
import com.davi_crawler.traffic.entity.BusRoutePathEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRouteLinkRepository extends JpaRepository<BusRouteLinkEntity, Long> {
}
