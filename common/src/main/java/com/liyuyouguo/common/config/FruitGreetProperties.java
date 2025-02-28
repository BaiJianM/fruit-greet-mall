package com.liyuyouguo.common.config;

import com.liyuyouguo.common.config.wx.WeChatProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 系统通用配置
 *
 * @author baijianmin
 */
@Data
@ConfigurationProperties(prefix = "fruit-shop")
public class FruitGreetProperties {

    /**
     * 上传文件路径
     */
    private String uploadPath;

    /**
     * 项目域名
     */
    private String domain;

    /**
     * 微信配置
     */
    private WeChatProperties wx;



}
