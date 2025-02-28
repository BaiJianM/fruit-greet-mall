package com.liyuyouguo.common.config.qiniu;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author baijianmin
 */
@Data
@ConfigurationProperties(prefix = "fruit-shop.qiniu")
public class QiNiuProperties {

    private String accessKey;

    private String secretKey;

    private String bucket;

    private String domain;

}
