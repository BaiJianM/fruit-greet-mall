package com.liyuyouguo.common.config.web.security.jwt;

import com.alibaba.fastjson2.JSON;
import com.liyuyouguo.common.beans.UserInfo;
import com.liyuyouguo.common.commons.Constants;
import com.liyuyouguo.common.config.web.security.SecurityProperties;
import com.liyuyouguo.common.utils.RedisUtils;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.Arrays;

/**
 * JWT身份验证过滤器
 *
 * @author baijianmin
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final RedisUtils redisUtils;

    private final SecurityProperties securityProperties;

    private static final String SUFFIX = "/**";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取认证信息
        final String authHeader = request.getHeader(Constants.SystemInfo.AUTHORIZATION);
        final String jwt;
        // 将请求包装成可重复读取
        ContentCachingRequestWrapper wrapper = new ContentCachingRequestWrapper(request);
        // 是否带有token
        boolean validUrl = authHeader == null || !authHeader.startsWith(Constants.SystemInfo.AUTHORIZATION_PREFIX);
        log.info("请求的资源: {}", request.getRequestURI());
        // 是否为白名单中的uri
        boolean isWhite = Arrays.stream(securityProperties.getIgnoreUrls())
                .anyMatch(ig -> {
                    boolean matchAll = ig.equals(request.getRequestURI());
                    boolean matchPrefix = false;
                    try {
                        if (ig.contains(SUFFIX)) {
                            matchPrefix = request.getRequestURI().startsWith(ig.substring(0, ig.indexOf(SUFFIX)));
                        }
                    } catch (Exception e) {
                        log.error("白名单匹配失败，错误信息: {}", e.getMessage());
                    }
                    return matchAll || matchPrefix;
                });
        if (validUrl && isWhite) {
            filterChain.doFilter(wrapper, response);
            return;
        }
        // 获取其中的token信息
        if (StringUtils.isBlank(authHeader)) {
            log.error("请求头中未携带Token信息");
            return;
        }
        jwt = authHeader.substring(Constants.SystemInfo.AUTHORIZATION_PREFIX.length());
        if (StringUtils.isNotBlank(jwt) && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 判断token是否过期
            boolean tokenExpired;
            try {
                tokenExpired = jwtService.isTokenExpired(jwt);
            } catch (Exception e) {
                log.error("非法的Token: {}", jwt);
                return;
            }
            if (!tokenExpired) {
                // 从token中解析出username
                final String username = jwtService.extractUsername(jwt);
                // 判断对应用户的缓存是否存在
                String key = Constants.LoginUser.LOGIN_USER_PREFIX + username;
                boolean isTokenValid = redisUtils.hasKey(key).orElse(false);
                if (isTokenValid) {
                    redisUtils.get(key).ifPresent(user -> {
                        UserInfo userInfo = JSON.parseObject(user.toString(), UserInfo.class);
                        // token是否一致
                        if (userInfo.getToken().equals(jwt)) {
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(userInfo, null,
                                            userInfo.getAuthorities());
                            authentication.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(request));
                            // 更新安全上下文的持有用户
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        } else {
                            log.info("Jwt的本地token与Redis缓存token不一致");
                        }
                    });
                } else {
                    log.info("Redis缓存token已失效");
                }
            } else {
                log.info("Jwt的token已过期");
            }
            filterChain.doFilter(wrapper, response);
        }
    }
}