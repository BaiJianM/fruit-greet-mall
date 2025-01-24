package com.liyuyouguo.common.config.web.authcode;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 图形验证码配置
 *
 * @author baijianmin
 */
@Data
@ConfigurationProperties(prefix = "answer.kaptcha")
public class KaptchaProperties {

    /**
     * 是否开启图形验证码验证
     */
    private Boolean isEnable = false;

    /**
     * 图形验证码类型，默认数字类型
     */
    private AuthCodeTypeEnum authCodeType = AuthCodeTypeEnum.MATH;

    /**
     * 图形验证码过期时间，默认五分钟
     */
    private Integer timeout = 5;
}
