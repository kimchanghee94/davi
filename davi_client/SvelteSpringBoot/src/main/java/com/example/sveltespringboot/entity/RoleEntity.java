package com.example.sveltespringboot.entity;

import com.example.sveltespringboot.dto.RoleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="DV_ROLE")
public class RoleEntity {
    @Id
    @Column(name="ROLE_CODE_PK")
    private int roleCode;

    @Column(name="ROLE_NAME")
    private String roleName;

    public RoleDto toDto(){
        RoleDto roleDto = RoleDto.builder()
                .roleCode(roleCode)
                .roleName(roleName)
                .build();
        return roleDto;
    }
}
