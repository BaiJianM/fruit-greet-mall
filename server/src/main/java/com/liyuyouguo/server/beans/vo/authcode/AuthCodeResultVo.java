package com.liyuyouguo.server.beans.vo.authcode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 图形验证码返回值
 *
 * @author baijianmin
 */
@Data
@Schema(description = "图形验证码信息")
public class AuthCodeResultVo {

    @Schema(description = "是否开启图形验证码", example = "true")
    private Boolean captchaEnabled;

    @Schema(description = "验证码缓存key", example = "123456")
    private String uuid;

    @Schema(description = "验证码图片base64字符串")
    private String img;
}
