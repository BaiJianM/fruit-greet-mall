package com.liyuyouguo.common.config.web.security;

import com.liyuyouguo.common.commons.SystemError;
import com.liyuyouguo.common.config.web.security.handler.AuthenticationFailureHandlerImpl;
import com.liyuyouguo.common.config.web.security.handler.AuthenticationSuccessHandlerImpl;
import com.liyuyouguo.common.config.web.security.handler.LogoutSuccessHandlerImpl;
import com.liyuyouguo.common.config.web.security.jwt.JwtAuthenticationFilter;
import com.liyuyouguo.common.config.web.security.jwt.JwtProperties;
import com.liyuyouguo.common.config.web.security.jwt.JwtService;
import com.liyuyouguo.common.service.security.AuthenticationService;
import com.liyuyouguo.common.utils.RedisUtils;
import com.liyuyouguo.common.utils.SecurityRenderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * spring security配置
 *
 * @author baijianmin
 */
@Configuration
@EnableConfigurationProperties({JwtProperties.class, SecurityProperties.class})
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final SecurityProperties securityProperties;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final RedisUtils redisUtils;

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    /**
     * 配置鉴权过滤链
     *
     * @param http http鉴权器
     * @return SecurityFilterChain 鉴权链
     * @throws Exception 异常信息
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                // options请求放行
                                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                                // 无需鉴权的url
                                .requestMatchers(securityProperties.getIgnoreUrls()).permitAll()
                                .anyRequest().authenticated())
                // 身份验证器
                .authenticationProvider(authenticationProvider())
                // 添加JWT过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 登录
                .formLogin(login -> {
                    login.successHandler(new AuthenticationSuccessHandlerImpl(authenticationService));
                    login.failureHandler(new AuthenticationFailureHandlerImpl());
                })
                // 登出
                .logout(logout -> logout.logoutSuccessHandler(new LogoutSuccessHandlerImpl(redisUtils, jwtService)))
                // 前后端分离，不需要csrf保护
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用basic明文验证
                .httpBasic(AbstractHttpConfigurer::disable)
                // 跨域配置
                .cors(c -> c.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    // 允许所有域名进行跨域调用
                    List<String> allowList = Arrays.asList("http://127.0.0.1", "http://localhost",
                            "http://localhost:9528",
                            "http://8.138.177.7");
                    corsConfiguration.setAllowedOriginPatterns(allowList);
                    // 允许跨越发送cookie
                    corsConfiguration.setAllowCredentials(true);
                    // 放行全部原始头信息
                    corsConfiguration.addAllowedHeader("*");
                    // 允许所有请求方法跨域调用
                    corsConfiguration.addAllowedMethod("*");
                    corsConfiguration.setMaxAge(3600L);
                    return corsConfiguration;
                }))
                // 基于token，禁用session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(new AuthenticationEntryPointImpl())
                                .accessDeniedHandler(new AccessDeniedHandlerImpl()));
        return http.build();
    }

    /**
     * 无资源权限实现类
     */
    static class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                             AuthenticationException authException) throws IOException {
            switch (HttpStatus.valueOf(response.getStatus())) {
                case NOT_FOUND: {
                    SecurityRenderUtil.renderErrorResponse(response, HttpStatus.NOT_FOUND, SystemError.API_NOT_FOUND.getDescribe());
                    break;
                }
                case SERVICE_UNAVAILABLE: {
                    SecurityRenderUtil.renderErrorResponse(response, HttpStatus.SERVICE_UNAVAILABLE, SystemError.SERVICE_UNAVAILABLE.getDescribe());
                    break;
                }
                default: {
                    SecurityRenderUtil.renderErrorResponse(response, HttpStatus.UNAUTHORIZED, SystemError.TOKEN_WRONG.getDescribe());
                    break;
                }
            }
        }
    }

    /**
     * 禁止访问实现类
     */
    static class AccessDeniedHandlerImpl implements AccessDeniedHandler {

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response,
                           AccessDeniedException accessDeniedException) throws IOException {
            SecurityRenderUtil.renderErrorResponse(response, HttpStatus.FORBIDDEN, SystemError.UNAUTHORIZED.getDescribe());
        }
    }

    /**
     * 构建身份校验机制、身份验证提供程序
     *
     * @return AuthenticationProvider 身份校验机制、身份验证提供程序
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // 创建一个用户认证提供者
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // 设置用户相信信息，可以从数据库中读取、或者缓存、或者配置文件
        authProvider.setUserDetailsService(userDetailsService);
        // 设置加密机制，若想要尝试对用户进行身份验证，我们需要知道使用的是什么编码
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * 基于用户名和密码或使用用户名和密码进行身份验证
     *
     * @param config 身份验证器配置
     * @return AuthenticationManager 身份验证管理器
     * @throws Exception 异常信息
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}