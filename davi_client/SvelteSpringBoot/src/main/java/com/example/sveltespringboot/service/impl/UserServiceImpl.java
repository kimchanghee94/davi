package com.example.sveltespringboot.service.impl;

import com.example.sveltespringboot.Provider.JwtTokenProvider;
import com.example.sveltespringboot.dto.RoleDto;
import com.example.sveltespringboot.dto.TokenDto;
import com.example.sveltespringboot.dto.UserDto;
import com.example.sveltespringboot.entity.RoleEntity;
import com.example.sveltespringboot.entity.UserEntity;
import com.example.sveltespringboot.handler.RoleDataHandler;
import com.example.sveltespringboot.handler.UserDataHandler;
import com.example.sveltespringboot.service.UserService;
import io.jsonwebtoken.Jwts;
import jdk.nashorn.internal.parser.Token;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    UserDataHandler userDataHandler;
    RoleDataHandler roleDataHandler;
    private final PasswordEncoder encoder;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public UserServiceImpl(UserDataHandler userDataHandler, RoleDataHandler roleDataHandler,
                           PasswordEncoder encoder, AuthenticationManagerBuilder authenticationManagerBuilder,
                           JwtTokenProvider jwtTokenProvider, RedisTemplate<String, Object> redisTemplate){
        this.userDataHandler = userDataHandler;
        this.roleDataHandler = roleDataHandler;
        this.encoder = encoder;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public int refreshTokenCheck(String refreshToken){
        //클라이언트로부터 받은 refreshToken 유효성 체크

        if(jwtTokenProvider.afterValidateToken(refreshToken) != 1){
            return 1;
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

        //Redis에서 Refresh Token 가져오와서 유효성 체크
        String redisRefreshToken = (String) redisTemplate.opsForValue().get("RefreshToken:"+authentication.getName());
        if(!redisRefreshToken.equals(refreshToken)){
            return 1;
        }
        return 0;
    }

    @Transactional
    @Override
    public TokenDto refreshToken(TokenDto tokenDto){
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();

        if(jwtTokenProvider.afterValidateToken(refreshToken) != 1){
            return null;
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        //Redis에서 Refresh Token 가져오기
        String redisRefreshToken = (String) redisTemplate.opsForValue().get("RefreshToken:"+authentication.getName());
        if(!redisRefreshToken.equals(refreshToken)){
            return null;
        }

        TokenDto newTokenDto = jwtTokenProvider.generateToken(authentication);
        redisTemplate.opsForValue().set("RefreshToken:" + authentication.getName(), newTokenDto.getRefreshToken(),
                newTokenDto.getRefreshTokenExpiresTime(), TimeUnit.MILLISECONDS);

        return newTokenDto;

//사용자 DB에 refreshToken을 넣어 처리했을 때
//        if(jwtTokenProvider.validateToken(refreshToken) == 1){
//            String subName = jwtTokenProvider.getTokenSubjectName(refreshToken);
//
//            if(subName != null){
//                UserEntity userEntity = userDataHandler.getUserEntity(subName);
//                System.out.println(userEntity.getRefreshToken());
//                if(userEntity.getRefreshToken().equals(refreshToken)){
//                    //accessToken의 재발급과 추가적으로 보안을 위해 refreshToken도 새로 발급해준다.
//                    Long accessTime = 10*1000L;
//                    Long refreshTime = 60*1000L;
//
//                    String newAccessToken = jwtTokenProvider.createToken(subName, "ROLE_" + userEntity.getRoleEntity().getRoleName(), accessTime);
//                    String newRefreshToken = jwtTokenProvider.createToken(subName, "ROLE_" + userEntity.getRoleEntity().getRoleName(), refreshTime);
//
//                    TokenDto newTokenDto = new TokenDto("Bearer", newAccessToken, newRefreshToken);
//
//                    updateRefreshToken(subName, newRefreshToken);
//
//                    return newTokenDto;
//                }
//            }else{
//                //사실상 첫번째 조건 분기에서 refreshToken만료 처리에 대한 확인이 끝난 상태라
//                //해당 조건에서는 refreshToken에 대한 사용자 id를 무조건 가져올 수 밖에 없음
//                //따라서 굳이 null값처리가 불필요할 수도 있다.
//               return null;
//            }
//
//
//            return tokenDto;
//        }else{
//            return null;
//        }
    }


//    @Override
//    public void updateRefreshToken(String userId, String refreshToken){
//        userDataHandler.updateRefreshToken(userId, refreshToken);
//    }

    @Transactional
    public TokenDto login(String userId, String passwd){
        // 1. Login ID/PW를 기반으로 Authentication 객체 생성
        // 이때 authentication는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, passwd);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 메서드가 실행될 때 CustomUserDetailService에서 만든 loadUserByUsername 메서드가 실행
        System.out.println("authenticationToken "+ authenticationToken);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        System.out.println("패스워드가 실패할 경우 예외처리로 끝나면서 해당구간은 타지 않는다.");

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        redisTemplate.opsForValue().set("RefreshToken:" + userId, tokenDto.getRefreshToken(),
                tokenDto.getRefreshTokenExpiresTime(), TimeUnit.MILLISECONDS);
        //사용자DB에 RefreshToken을 넣을 경우
        //updateRefreshToken(userId, tokenDto.getRefreshToken());

        return tokenDto;
    }

    @Override
    public int logout(TokenDto tokenDto){
        String accessToken = tokenDto.getAccessToken();

        //logout할 access 토큰이 유효한지 검증
        if(jwtTokenProvider.validateToken(accessToken) != 1){
            return 1;
        }

        //Access Token에서 User email을 가져온다.
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        //Redis에서 해당 Id로 저장된 Refresh Token이 있는 지 여부 확인 후 삭제
        if(redisTemplate.opsForValue().get("RefreshToken:"+authentication.getName()) != null){
            redisTemplate.delete("RefreshToken:"+authentication.getName());
        }

        // 해당 Access Token 유효시간을 가지고 와서 BlackList에 저장하기
        Long expiration = jwtTokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
        return 0;
    }

    @Override
    public void saveUser(String userId, String passwd, String phone, String name, java.sql.Date lastLoginDate,
                            int roleCode, java.sql.Date joinDate, java.sql.Date passwdChangeDate){
        RoleEntity roleEntity = roleDataHandler.getRoleEntity(roleCode);
        userDataHandler.saveUserEntity(userId,
                encoder.encode(passwd),
                phone, name, lastLoginDate, roleEntity, joinDate, passwdChangeDate);
    }

    @Override
    public UserDto getUser(String userId){
        try{
            UserEntity userEntity = userDataHandler.getUserEntity(userId);

            RoleDto roleDto = new RoleDto(userEntity.getRoleEntity().getRoleCode(), userEntity.getRoleEntity().getRoleName());

            UserDto userDto = new UserDto(userEntity.getUserId(), userEntity.getPasswd(), userEntity.getPhone(), userEntity.getName(), userEntity.getLastLoginDate(),
                    roleDto, userEntity.getJoinDate(), userEntity.getPasswdChangeDate());

            return userDto;
        }catch (EntityNotFoundException e){
            System.out.println("[" + userId + "] User Entity Not Find From USERS_TB");
            return null;
        }
    }
}
