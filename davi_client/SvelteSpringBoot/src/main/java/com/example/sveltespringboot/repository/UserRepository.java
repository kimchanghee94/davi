package com.example.sveltespringboot.repository;

import com.example.sveltespringboot.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
//    @Query(value = "SELECT u.refreshToken FROM UserEntity u WHERE u.userId = :userId")
//    String findRefreshToken(@Param("userId")String userId);

//    @Modifying
//    @Transactional
//    @Query(value = "UPDATE UserEntity u SET u.refreshToken = :refreshToken WHERE u.userId = :userId")
//    void updateRefreshToken(@Param("userId")String userId, @Param("refreshToken")String refreshToken);
}
