package com.example.sveltespringboot.Provider;

import com.example.sveltespringboot.config.SecurityUtil;
import com.example.sveltespringboot.dto.TokenDto;
import com.example.sveltespringboot.repository.UserRepository;
import com.example.sveltespringboot.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Long accessTokenExpiresTime = /*시간*/ /*분*/ 10*1000L/*초ms단위*/;
    private final Long refreshTokenExpiresTime = /*시간*/ 10*/*분*/ 60*1000L/*초ms단위*/;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            RedisTemplate<String, Object> redisTemplate) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.redisTemplate = redisTemplate;
    }

    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public TokenDto generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

//        long now = (new Date()).getTime();
//        // Access Token 생성
//        Date accessTokenExpiresIn = new Date(now + /*시간*/ /*분*/ 10*1000/*초ms단위*/);
        String accessToken = createToken(authentication.getName(), authorities, accessTokenExpiresTime);
//                Jwts.builder()
//                .setSubject(authentication.getName())
//                .claim("auth", authorities)
//                .setExpiration(accessTokenExpiresIn)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();

        // Refresh Token 생성
        String refreshToken = createToken(authentication.getName(), authorities, refreshTokenExpiresTime);
//                Jwts.builder()
//                .setSubject(authentication.getName())
//                .setExpiration(new Date(now + (24*/*시간*/ 60*/*분*/ 60*1000/*초ms단위*/)))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresTime(accessTokenExpiresTime)
                .refreshTokenExpiresTime(refreshTokenExpiresTime)
                .build();
    }

    // Create token
    public String createToken(String id, String roles, long tokenValid) {
        Claims claims = Jwts.claims().setSubject(id); // claims 생성 및 payload 설정
        claims.put("auth", roles); // 권한 설정, key/ value 쌍으로 저장

        Date date = new Date();

        return Jwts.builder()
                .setClaims(claims) // 발행 유저 정보 저장
                .setExpiration(new Date(date.getTime() + tokenValid)) // 토큰 유효 시간 저장
                .signWith(key, SignatureAlgorithm.HS256) // 해싱 알고리즘 및 키 설정
                .compact(); // 생성
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 정보를 검증하는 메서드
    public int validateToken(String token) {
        try {
           // System.out.println("Validate Token" + SecurityUtil.getCurrentMemberId());
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            System.out.println("Claims get Subject = "+claims.getSubject());

            String isLogout = (String) redisTemplate.opsForValue().get(token);
            if(!ObjectUtils.isEmpty(isLogout)){
                System.out.println("로그아웃 된 사용자입니다.");
                throw new JwtException("Logout JWT Token");
            }

            return 1;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new JwtException("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new JwtException("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new JwtException("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            throw new JwtException("JWT claims string is empty.");
        }
    }

    //위는 초기 요청이 들어오고 헤더에 들어있는 값을 측정할 때 사용하므로 커스텀한 JwtExceptionFilter 클래스로 간다.
    // 아래의 경우는 post 바디에 있는 값을 추후에 다루게 되므로 Exception을 던져서 사용하면 안된다
    public int afterValidateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            System.out.println("After valid Check - Claims get Subject = " + claims.getSubject());
            return 1;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return 0;
    }

//    public String getTokenSubjectName(String token){
//        try{
//            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
//            return claims.getSubject();
//        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//            log.info("Invalid JWT Token", e);
//        } catch (ExpiredJwtException e) {
//            log.info("Expired JWT Token", e);
//        } catch (UnsupportedJwtException e) {
//            log.info("Unsupported JWT Token", e);
//        } catch (IllegalArgumentException e) {
//            log.info("JWT claims string is empty.", e);
//        }
//        return null;
//    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getExpiration(String accessToken){
        Date expiration = this.parseClaims(accessToken).getExpiration();
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}