package com.liyuyouguo.common.config.web.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * spring security自定义配置
 *
 * @author baijianmin
 */
@Data
@ConfigurationProperties(prefix = "spring.security")
public class SecurityProperties {

    /**
     * 无需鉴权的url
     */
    private String[] ignoreUrls;

}
