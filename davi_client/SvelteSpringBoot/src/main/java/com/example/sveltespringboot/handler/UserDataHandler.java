package com.example.sveltespringboot.handler;

import com.example.sveltespringboot.entity.RoleEntity;
import com.example.sveltespringboot.entity.UserEntity;

public interface UserDataHandler {
    void saveUserEntity(String userId, String passwd, String phone, String name, java.sql.Date lastLoginDate,
                        RoleEntity roleEntity, java.sql.Date joinDate, java.sql.Date passwdChangeDate);
    UserEntity getUserEntity(String userId);

//    void updateRefreshToken(String userId, String refreshToken);
}
