package com.davi_crawler.molit.repository;

import com.davi_crawler.molit.entity.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<LinkEntity, Long> {
}
