//package com.example.sveltespringboot.config;
//
//import com.example.sveltespringboot.Filter.JwtAuthenticationFilter;
//import com.example.sveltespringboot.Provider.JwtTokenProvider;
//import com.example.sveltespringboot.service.impl.CustomUserDetailsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//
//@RequiredArgsConstructor
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    private final AuthenticationFailureHandler customFailureHandler;
//    private final CustomUserDetailsService customUserDetailService;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Bean
//    public BCryptPasswordEncoder encoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//        auth.userDetailsService(customUserDetailService).passwordEncoder(encoder());
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception{
//        web.ignoring().antMatchers("/css/**","/js/**","/img/**");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception{
//        Object RestControllerBase;
//        http
//                .cors() //(1)
//                .and()
//                .csrf() //(2)
//                .disable()
//                .exceptionHandling() //(3)
//                //.authenticationEntryPoint(unauthorizedHandler)
//                .and()
//                .sessionManagement() //(4)
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests() // (5)
//                .antMatchers("/api/v1/user-api/**", "/**")
//                .permitAll()
//                .antMatchers("/dbtest")
//                .permitAll()
//                .and()
//                //.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .formLogin().disable().headers().frameOptions().disable();
//
//        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
//
//
//
////                .csrf().disable()
////                .cors().configurationSource(corsConfigurationSource())
////                .and()
////                .authorizeRequests()
////                .mvcMatchers(HttpMethod.OPTIONS, "/**")
////                .permitAll()
////                .antMatchers( "/api/v1/user-api/**", "/resources/**")
////                .permitAll() // 로그인 권한은 누구나, resources파일도 모든권한
////                .antMatchers("/dbtest")
//////                .hasRole("MEMBER")
////                .permitAll()
////                .and()
////                .formLogin()
//////                .loginPage("http://localhost:5170/login")
////                //.loginProcessingUrl("/api/v1/user-api/login")
////                .passwordParameter("passwd")
////                .defaultSuccessUrl("http://localhost:5170/home")
////                .failureHandler(customFailureHandler)
////                .and()
////                .logout()
////                .logoutUrl("/api/v1/user-api/logout")
////                .logoutSuccessUrl("http://localhost:5170/login")
////                .invalidateHttpSession(true)
////                .deleteCookies("JSESSIONID");
//
////                .and()
////                .sessionManagement()
////                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
////                .maximumSessions(1)
////                .maxSessionsPreventsLogin(false);
//
//
////                .httpBasic().disable()
////                .cors().configurationSource(corsConfigurationSource())
////                .and()
////                .csrf().disable().authorizeRequests()
////                .antMatchers("/api/v1/user-api/**")
////                .anonymous()
////                .antMatchers("/dbtest")
//////                .hasAnyRole("ROLE_MEMBER")
////                .permitAll()
////                .anyRequest()
////                .authenticated()
////                .and()
////                .formLogin()
////                .loginPage("http://localhost:5170/login")
////                .loginProcessingUrl("/api/v1/user-api/login")
////                .passwordParameter("passwd")
////                .defaultSuccessUrl("http://localhost:5170/home")
////                .failureHandler(customFailureHandler)
////                .and()
////                .logout()
////                .logoutSuccessUrl("http://localhost:5170/login")
////                .invalidateHttpSession(true);
//    }
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addAllowedOrigin("http://localhost:5170");
//        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
//        configuration.addAllowedHeader("Authorization");
//        configuration.setAllowCredentials(true);
//        configuration.setMaxAge(3600L);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
////        CorsConfiguration configuration = new CorsConfiguration();
////
////        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5170"));
////        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
////        configuration.setAllowedHeaders(Arrays.asList("Origin","Accept","X-Requested-With",
////                "Content-Type","Access-Control-Request-Method",
////                "Access-Control-Request-Headers","Authorization"));
////        //configuration.setExposedHeaders(Arrays.asList("Set-Cookie"));
////
////        //configuration.addAllowedHeader("*");
////        //configuration.addAllowedMethod("*");
////        configuration.setAllowCredentials(true);
////
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        source.registerCorsConfiguration("/**", configuration);
////        return source;
//    }
//
//}