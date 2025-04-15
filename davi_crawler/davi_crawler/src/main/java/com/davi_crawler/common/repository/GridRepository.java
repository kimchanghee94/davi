package com.davi_crawler.common.repository;

import com.davi_crawler.common.entity.GridEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GridRepository extends JpaRepository<GridEntity, Long> {
}
