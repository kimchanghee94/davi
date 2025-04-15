package com.example.sveltespringboot.dao;

import com.example.sveltespringboot.entity.RoleEntity;

public interface RoleDAO {
    RoleEntity getRole(int roleCode);
}
