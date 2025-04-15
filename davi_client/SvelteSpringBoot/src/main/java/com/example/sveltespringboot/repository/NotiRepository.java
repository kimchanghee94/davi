package com.example.sveltespringboot.repository;

import com.example.sveltespringboot.entity.NotiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotiRepository extends JpaRepository<NotiEntity, Long> {
}
