package com.example.sveltespringboot.repository;

import com.example.sveltespringboot.entity.APIReqEntity;
import com.example.sveltespringboot.entity.APIResEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface APIResRepository extends JpaRepository<APIResEntity, Long> {
    //apiServiceId를 가지고 Res칼럼 항목들을 가져온다.
    List<APIResEntity> findAllByApiServiceId(Long apiServiceId);
}
