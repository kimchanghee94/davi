package com.example.sveltespringboot.entity;

import com.example.sveltespringboot.dto.RoleDto;
import com.example.sveltespringboot.dto.UserDto;
import lombok.*;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="DV_USERS")
public class UserEntity {

//    public UserEntity(String userId, String passwd, String phone, String name,
//                      java.sql.Date lastLoginDate, RoleEntity roleEntity, java.sql.Date joinDate,
//                      java.sql.Date passwdChangeDate){
//        this.userId = userId;
//        this.passwd = passwd;
//        this.phone = phone;
//        this.name = name;
//        this.lastLoginDate = lastLoginDate;
//        this.roleEntity = roleEntity;
//        this.joinDate = joinDate;
//        this.passwdChangeDate = passwdChangeDate;
//    }

    @Id
    @Column(name="USER_ID_PK")
    private String userId;

    @Column(name="PASSWD")
    private String passwd;

    @Column(name="PHONE")
    private String phone;

    @Column(name="NAME")
    private String name;

    @Column(name="LAST_LOGIN_DATE")
    private java.sql.Date lastLoginDate;

    @OneToOne
    @JoinColumn(name="ROLE_CODE_PK")
    private RoleEntity roleEntity;

    @Column(name="JOIN_DATE")
    private java.sql.Date joinDate;

    @Column(name="PASSWD_CHANGE_DATE")
    private java.sql.Date passwdChangeDate;

//    @Column(name="REFRESH_TOKEN")
//    private String refreshToken;

    public UserDto toDto(){
        UserDto userDto = UserDto.builder()
                .userId(userId)
                .passwd(passwd)
                .phone(phone)
                .name(name)
                .lastLoginDate(lastLoginDate)
                .roleDto(new RoleDto(roleEntity.getRoleCode(), getRoleEntity().getRoleName()))
                .joinDate(joinDate)
                .passwdChangeDate(passwdChangeDate)
             //   .refreshToken(refreshToken)
                .build();
        return userDto;
    }
}
