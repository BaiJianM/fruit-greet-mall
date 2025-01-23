package com.liyuyouguo.server.config.web.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * JWT工具服务类
 *
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    /**
     * jwt自定义配置
     */
    private final JwtProperties properties;

    /**
     * 生成token
     *
     * @param userInfo 待生成用户信息
     * @return String token令牌
     */
    public String generateToken(String userInfo) {
        Claims claims = Jwts.claims();
        claims.setSubject(userInfo);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * properties.getExpire()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析token字符串中的加密信息【加密算法&加密密钥】, 提取所有声明的方法
     *
     * @param token 令牌字符串
     * @return Claims jwt信息对象
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                // 获取alg开头的信息
                .setSigningKey(getSignInKey())
                .build()
                // 解析token字符串
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取签名密钥的方法
     *
     * @return Key 基于指定的密钥字节数组创建用于HMAC-SHA算法的新SecretKey实例
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(properties.getSecurityKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 解析token字符串中的权限信息
     *
     * @param token 令牌字符串
     * @return T 权限信息
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 从token中解析出username
     *
     * @param token 令牌字符串
     * @return String 登录的username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 验证token是否过期
     *
     * @param token 令牌字符串
     * @return boolean token是否过期
     */
    public boolean isTokenExpired(String token) {
        boolean isExpired;
        try {
            isExpired = extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            isExpired = true;
        }
        return isExpired;
    }

    /**
     * 从授权信息中获取token过期时间
     *
     * @param token 令牌字符串
     * @return Date token过期时间
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}