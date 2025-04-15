package com.davi_crawler.public_data_portal.repository;

import com.davi_crawler.public_data_portal.dto.PublicDataPortalServiceDto;
import com.davi_crawler.public_data_portal.entity.PublicDataPortalServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PublicDataPortalServiceRepository extends JpaRepository<PublicDataPortalServiceEntity, Long> {
    //serviceDto의 apiName을 가지고 apiId값을 가져온다.
    @Query(value = "SELECT a.publicDataPortalServiceId FROM PublicDataPortalServiceEntity a WHERE a.publicDataPortalId = :#{#publicDataPortalServiceDto.publicDataPortalId} AND a.route = :#{#publicDataPortalServiceDto.route}")
    Long selectPublicDataPortalId(@Param("publicDataPortalServiceDto") PublicDataPortalServiceDto publicDataPortalServiceDto) throws Exception;

    Long findByPublicDataPortalIdAndRoute(Long publicDataPortalId, String route);

    //publicDatatPortalId를 가지고 routeURL항목들을 가져온다.
    List<PublicDataPortalServiceEntity> findAllByPublicDataPortalId(Long publicDatatPortalId);
}
