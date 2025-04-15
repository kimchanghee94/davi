package com.davi_crawler.molit.repository;

import com.davi_crawler.molit.entity.EupMyeonDongEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EupMyeonDongRepository extends JpaRepository<EupMyeonDongEntity, Long> {
    List<EupMyeonDongEntity> findAllByFullNmLike(String fullNm) throws Exception;

    EupMyeonDongEntity findByEmdCd(String emdCd) throws Exception;
}
