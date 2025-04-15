package com.example.sveltespringboot.handler.impl;

import com.example.sveltespringboot.dao.RoleDAO;
import com.example.sveltespringboot.entity.RoleEntity;
import com.example.sveltespringboot.handler.RoleDataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleDataHandlerImpl implements RoleDataHandler {
    RoleDAO roleDAO;

    @Autowired
    public RoleDataHandlerImpl(RoleDAO roleDAO){
        this.roleDAO = roleDAO;
    }

    @Override
    public RoleEntity getRoleEntity(int roleCode){
        return roleDAO.getRole(roleCode);
    }
}
