package com.example.sveltespringboot.dto;

import com.example.sveltespringboot.entity.RoleEntity;
import lombok.*;

import javax.management.relation.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RoleDto {
    private int roleCode;
    private String roleName;

    public RoleEntity toEntity(){
        RoleEntity roleEntity = RoleEntity.builder()
                .roleCode(roleCode)
                .roleName(roleName)
                .build();
        return roleEntity;
    }
}
