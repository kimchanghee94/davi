package com.example.sveltespringboot.repository;

import com.example.sveltespringboot.dto.APIDto;
import com.example.sveltespringboot.entity.APIEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface APIRepository extends JpaRepository<APIEntity, Long> {

    //APIDto의 apiName을 가지고 apiId값을 가져온다.
    @Query(value = "SELECT a.apiId FROM APIEntity a WHERE a.apiSiteUrl = :#{#apiDto.apiSiteUrl}")
    Long selectApiId(@Param("apiDto")APIDto apiDto);

    APIEntity findByApiIdAndTableJsonFlag(Long apiId, String tableJsonFlag);

    APIEntity findByApiName(String apiName);

    List<APIEntity> findTop10ByTableJsonFlagOrderByApiIdAsc(String tableJsonFlag);
}
