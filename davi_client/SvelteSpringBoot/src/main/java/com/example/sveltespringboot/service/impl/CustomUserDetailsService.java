package com.example.sveltespringboot.service.impl;

import com.example.sveltespringboot.entity.UserEntity;
import com.example.sveltespringboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findById(username).orElseThrow(
                () -> new UsernameNotFoundException("해당 사용자가 존재하지 않습니다. : " + username)
        );

        UserDetails userDetails = createUserDetails(new CustomUserDetails(userEntity));
        System.out.println("This is UserDetails" + userDetails.getUsername() + ", " + userDetails.getPassword());
        return userDetails;
        
//        return userRepository.findById(username)
//                .map(this::createUserDetails)
//                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자가 존재하지 않습니다."));
    }

    // 해당하는 User의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴한다.
    private UserDetails createUserDetails(CustomUserDetails customUserDetails){
        System.out.println("Load User By User Name Test2"
        + customUserDetails.getUsername() + customUserDetails.getAuthorities().toString());

        //아래의 방법으로 사용하게 될 경우 DB의 ROLE_NAME은 ROLE_의 접두사를 사용해서 넣으면 안된다.
        //알아서 넣어주기 때문
        return User.builder()
                .username(customUserDetails.getUsername())
                .password(customUserDetails.getPassword())
                .roles(customUserDetails.getUserEntity().getRoleEntity().getRoleName())
                .build();
    }

//  아래는 세션을 이용했을 때 필요한 과정이다.
//    private final HttpSession session;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
//        System.out.println("Before 사용자 존재?" + username);
//
//        UserEntity userEntity = userRepository.findById(username).orElseThrow(
//                () -> new UsernameNotFoundException("해당 사용자가 존재하지 않습니다. : " + username)
//        );
//        System.out.println("After 사용자 존재?" + userEntity.getUserId());
//        session.setAttribute("user", new UserSessionDto(userEntity));
//
//        return new CustomUserDetails(userEntity);
//    }
}
