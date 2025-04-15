package com.example.sveltespringboot.handler.impl;

import com.example.sveltespringboot.dao.UserDAO;
import com.example.sveltespringboot.entity.RoleEntity;
import com.example.sveltespringboot.entity.UserEntity;
import com.example.sveltespringboot.handler.UserDataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDataHandlerImpl implements UserDataHandler {
    UserDAO userDAO;

    @Autowired
    public UserDataHandlerImpl(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @Override
    public void saveUserEntity(String userId, String passwd, String phone, String name, java.sql.Date lastLoginDate,
                               RoleEntity roleEntity, java.sql.Date joinDate, java.sql.Date passwdChangeDate){
        UserEntity userEntity = new UserEntity(userId, passwd, phone, name, lastLoginDate, roleEntity, joinDate, passwdChangeDate);
        userDAO.saveUser(userEntity);
    }

    @Override
    public UserEntity getUserEntity(String userId){
        return userDAO.getUser(userId);
    }

//    @Override
//    public void updateRefreshToken(String userId, String refreshToken){
//        userDAO.updateRefreshToken(userId, refreshToken);
//    }
}
