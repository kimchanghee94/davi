package com.example.sveltespringboot.dto;

import com.example.sveltespringboot.entity.UserEntity;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserSessionDto implements Serializable {
    private String username;
    private String password;
    private String phone;
    private String name;
    private java.sql.Date lastLoginDate;
    private RoleDto roleDto;
    private java.sql.Date joinDate;
    private java.sql.Date passwdChangeDate;

//    private String refreshToken;

    public UserSessionDto(UserEntity userEntity){
        this.username = userEntity.getUserId();
        this.password = userEntity.getPasswd();
        this.phone = userEntity.getPhone();
        this.name = userEntity.getName();
        this.lastLoginDate = userEntity.getLastLoginDate();
        this.roleDto = userEntity.getRoleEntity().toDto();
        this.joinDate = userEntity.getJoinDate();
        this.passwdChangeDate = userEntity.getPasswdChangeDate();
//        this.refreshToken = userEntity.getRefreshToken();
    }
}
