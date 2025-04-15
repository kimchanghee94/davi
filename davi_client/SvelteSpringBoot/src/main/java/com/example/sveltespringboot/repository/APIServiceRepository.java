package com.example.sveltespringboot.repository;

import com.example.sveltespringboot.dto.APIDto;
import com.example.sveltespringboot.dto.APIServiceDto;
import com.example.sveltespringboot.entity.APIServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface APIServiceRepository extends JpaRepository<APIServiceEntity, Long> {
    //APIServiceDto의 apiName을 가지고 apiId값을 가져온다.
    @Query(value = "SELECT a.apiServiceId FROM APIServiceEntity a WHERE a.apiId = :#{#apiServiceDto.apiId} AND a.route = :#{#apiServiceDto.route}")
    Long selectApiId(@Param("apiServiceDto") APIServiceDto apiServiceDto);

    //ApiId를 가지고 routeURL항목들을 가져온다.
    List<APIServiceEntity> findAllByApiId(Long apiId);
}