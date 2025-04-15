package com.davi_crawler.public_data_portal.repository;

import com.davi_crawler.public_data_portal.entity.PublicDataPortalReqEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicDataPortalReqRepository extends JpaRepository<PublicDataPortalReqEntity, Long> {
    //serviceId를 가지고 Req칼럼 항목들을 가져온다.
    List<PublicDataPortalReqEntity> findAllByPublicDataPortalServiceId(Long publicDataPortalServiceId);
}
