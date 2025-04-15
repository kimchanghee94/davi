package com.example.sveltespringboot.repository;

import com.example.sveltespringboot.entity.APIReqEntity;
import com.example.sveltespringboot.entity.APIServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface APIReqRepository extends JpaRepository<APIReqEntity, Long> {
    //apiServiceId를 가지고 Req칼럼 항목들을 가져온다.
    List<APIReqEntity> findAllByApiServiceId(Long apiServiceId);
}
