package com.example.sveltespringboot.service.impl;

import com.example.sveltespringboot.entity.UserEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;

@Data
public class CustomUserDetails implements UserDetails {
    private final UserEntity userEntity;

    @Override
    public String getUsername(){
        return userEntity.getUserId();
    }

    @Override
    public String getPassword(){
        return userEntity.getPasswd();
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        Collection<GrantedAuthority> collectors = new ArrayList<>();
        collectors.add(() -> userEntity.getRoleEntity().getRoleName());

        return collectors;
    }
}
