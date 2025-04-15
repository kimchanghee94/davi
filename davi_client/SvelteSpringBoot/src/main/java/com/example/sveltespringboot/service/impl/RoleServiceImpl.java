package com.example.sveltespringboot.service.impl;

import com.example.sveltespringboot.dto.RoleDto;
import com.example.sveltespringboot.entity.RoleEntity;
import com.example.sveltespringboot.handler.RoleDataHandler;
import com.example.sveltespringboot.service.RoleService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class RoleServiceImpl implements RoleService {
    RoleDataHandler roleDataHandler;

    @Autowired
    public RoleServiceImpl(RoleDataHandler roleDataHandler){
        this.roleDataHandler = roleDataHandler;
    }

    @Override
    public String getRole(int roleCode){
        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();

        try{
            RoleEntity roleEntity = roleDataHandler.getRoleEntity(roleCode);
            RoleDto roleDto = new RoleDto(roleEntity.getRoleCode(), roleEntity.getRoleName());

            header.put("statusCode", "00");
            header.put("msg", "Get [" + roleCode + "] from ROLE_TB SUCCESS.");

        }catch(EntityNotFoundException e){
            System.out.println("[" + roleCode + "] Role Entity Not Find From ROLE_TB");
            header.put("statusCode", "01");
            header.put("msg", "Get [" + roleCode + "] from ROLE_TB FAILED.");
        }
        root.put("header", header);
        System.out.println(root.toString());
        return root.toJSONString();
    }
}
