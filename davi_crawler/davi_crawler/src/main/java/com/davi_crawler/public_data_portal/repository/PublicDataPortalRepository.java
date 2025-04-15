package com.davi_crawler.public_data_portal.repository;

import com.davi_crawler.public_data_portal.dto.PublicDataPortalDto;
import com.davi_crawler.public_data_portal.entity.PublicDataPortalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PublicDataPortalRepository extends JpaRepository<PublicDataPortalEntity, Long> {
    //api 사이트 주소를 가지고 apiId값을 가져온다.
    @Query(value = "SELECT a.publicDataPortalId FROM PublicDataPortalEntity a WHERE a.apiSiteUrl = :#{#publicDataPortalDto.apiSiteUrl}")
    Long selectPublicDataPortalId(@Param("publicDataPortalDto") PublicDataPortalDto publicDataPortalDto) throws Exception;

    PublicDataPortalEntity findByPublicDataPortalIdAndTableJsonFlag(Long publicDataPortalId, String tableJsonFlag);
}
