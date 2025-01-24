package com.liyuyouguo.common.controller;

import com.liyuyouguo.common.annotations.FruitShopController;
import com.liyuyouguo.common.beans.vo.authcode.AuthCodeResultVo;
import com.liyuyouguo.common.commons.FruitShopResponse;
import com.liyuyouguo.common.service.CaptchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 验证码操作控制层
 *
 * @author baijianmin
 */
@Slf4j
@Tag(name = "图形验证码相关接口")
@FruitShopController("/auth/code")
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaService captchaService;

    // TODO 不可无限调用，需后端配合前端增加调用三次后锁住1分钟的逻辑，后端锁住接口，前端增加解锁倒计时
    @Operation(summary = "获取图形验证码")
    @GetMapping
    public FruitShopResponse<AuthCodeResultVo> getCode() {
        return FruitShopResponse.success(captchaService.getAuthCode());
    }
}
