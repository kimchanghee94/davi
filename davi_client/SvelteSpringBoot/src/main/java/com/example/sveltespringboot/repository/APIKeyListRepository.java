package com.example.sveltespringboot.repository;

import com.example.sveltespringboot.entity.APIColumnListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface APIKeyListRepository extends JpaRepository<APIColumnListEntity, Long> {
}
