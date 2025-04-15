package com.example.sveltespringboot.service;

import com.example.sveltespringboot.dto.TokenDto;
import com.example.sveltespringboot.dto.UserDto;

public interface UserService {
    void saveUser(String userId, String passwd, String phone, String name, java.sql.Date lastLoginDate,
    int roleCode, java.sql.Date joinDate, java.sql.Date passwdChangeDate);

    UserDto getUser(String userId);

    TokenDto login(String userId, String passwd);

    int logout(TokenDto tokenDto);

//    void updateRefreshToken(String userId, String refreshToken);

    TokenDto refreshToken(TokenDto tokenDto);

    int refreshTokenCheck(String refreshToken);
}
