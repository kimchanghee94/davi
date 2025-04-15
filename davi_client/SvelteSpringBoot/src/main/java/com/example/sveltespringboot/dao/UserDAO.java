package com.example.sveltespringboot.dao;

import com.example.sveltespringboot.entity.UserEntity;

public interface UserDAO {
    void saveUser(UserEntity userEntity);

    UserEntity getUser(String userId);

//    void updateRefreshToken(String userId, String refreshToken);
}
