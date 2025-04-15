package com.example.sveltespringboot.dto;

import com.example.sveltespringboot.entity.RoleEntity;
import com.example.sveltespringboot.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDto {
//    public UserDto(String userId, String passwd, String phone, String name,
//                      java.sql.Date lastLoginDate, RoleDto roleDto, java.sql.Date joinDate,
//                      java.sql.Date passwdChangeDate){
//        this.userId = userId;
//        this.passwd = passwd;
//        this.phone = phone;
//        this.name = name;
//        this.lastLoginDate = lastLoginDate;
//        this.roleDto = roleDto;
//        this.joinDate = joinDate;
//        this.passwdChangeDate = passwdChangeDate;
//    }

    private String userId;

    private String passwd;

    private String phone;

    private String name;

    private java.sql.Date lastLoginDate;

    private RoleDto roleDto;

    private java.sql.Date joinDate;

    private java.sql.Date passwdChangeDate;

//    private String refreshToken;

    public UserEntity toEntity(){
        UserEntity userEntity = UserEntity.builder()
                .userId(userId)
                .passwd(passwd)
                .phone(phone)
                .name(name)
                .lastLoginDate(lastLoginDate)
                .roleEntity(new RoleEntity(roleDto.getRoleCode(), roleDto.getRoleName()))
                .joinDate(joinDate)
                .passwdChangeDate(passwdChangeDate)
//                .refreshToken(refreshToken)
                .build();
        return userEntity;
    }
}
