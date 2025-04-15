package com.davi_crawler.public_data_portal.repository;

import com.davi_crawler.public_data_portal.entity.PublicDataPortalReqEntity;
import com.davi_crawler.public_data_portal.entity.PublicDataPortalResEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicDataPortalResRepository extends JpaRepository<PublicDataPortalResEntity, Long> {
    //serviceId를 가지고 Res칼럼 항목들을 가져온다.
    List<PublicDataPortalResEntity> findAllByPublicDataPortalServiceId(Long publicDataPortalServiceId);
}
