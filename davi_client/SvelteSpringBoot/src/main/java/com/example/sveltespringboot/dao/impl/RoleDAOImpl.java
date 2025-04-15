package com.example.sveltespringboot.dao.impl;

import com.example.sveltespringboot.dao.RoleDAO;
import com.example.sveltespringboot.entity.RoleEntity;
import com.example.sveltespringboot.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleDAOImpl implements RoleDAO {
    RoleRepository roleRepository;

    @Autowired
    public RoleDAOImpl(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleEntity getRole(int roleCode){
        RoleEntity roleEntity = roleRepository.getById(roleCode);
        return roleEntity;
    }
}
