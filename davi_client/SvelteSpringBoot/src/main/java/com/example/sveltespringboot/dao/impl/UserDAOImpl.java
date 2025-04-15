package com.example.sveltespringboot.dao.impl;

import com.example.sveltespringboot.dao.UserDAO;
import com.example.sveltespringboot.entity.UserEntity;
import com.example.sveltespringboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDAOImpl implements UserDAO {
    UserRepository userRepository;

    @Autowired
    public UserDAOImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(UserEntity userEntity){
        userRepository.save(userEntity);
    }

    @Override
    public UserEntity getUser(String userId){
        UserEntity userEntity = userRepository.getById(userId);
        return userEntity;
    }

//    @Override
//    public void updateRefreshToken(String userId, String refreshToken){
//        userRepository.updateRefreshToken(userId, refreshToken);
//    }
}
