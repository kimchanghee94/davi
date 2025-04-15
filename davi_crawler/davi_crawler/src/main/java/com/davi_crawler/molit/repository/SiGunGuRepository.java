package com.davi_crawler.molit.repository;

import com.davi_crawler.molit.entity.SiGunGuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiGunGuRepository extends JpaRepository<SiGunGuEntity, Long> {
    SiGunGuEntity findByFullNmContainsAndSigKorNm(String fullNm, String sigKorNm) throws Exception;
}
