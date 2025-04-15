package com.example.sveltespringboot.repository;

import com.example.sveltespringboot.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
}
