package com.liyuyouguo.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 系统通用配置
 *
 * @author baijianmin
 */
@Data
@ConfigurationProperties(prefix = "fruit-shop")
public class FruitShopProperties {

    /**
     * 上传文件路径
     */
    private String uploadPath;

    /**
     * 项目域名
     */
    private String domain;

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
