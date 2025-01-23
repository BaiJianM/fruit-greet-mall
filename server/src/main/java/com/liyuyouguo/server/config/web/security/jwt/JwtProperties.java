package com.liyuyouguo.server.config.web.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * jwt自定义配置
 *
 * @author baijianmin
 */
@Data
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtProperties {

    /**
     * 密钥
     */
    private String securityKey;

    /**
     * token过期时间，单位: 秒
     */
    private Long expire;

}
