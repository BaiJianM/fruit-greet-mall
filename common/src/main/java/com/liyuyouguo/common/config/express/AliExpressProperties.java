package com.liyuyouguo.common.config.express;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author baijianmin
 */
@Data
@ConfigurationProperties(prefix = "fruit-shop.express.ali")
public class AliExpressProperties {

    private String url;

    private String appCode;

    private String appKey;

    private String appSecret;

}
