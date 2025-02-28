package com.liyuyouguo.common.config.wx;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author baijianmin
 */
@Data
@ConfigurationProperties(prefix = "fruit-shop.wx")
public class WeChatProperties {

    /**
     * 设置微信小程序的appid
     */
    private String appid;

    /**
     * 设置微信小程序的Secret
     */
    private String secret;

    private String appCode;

}
