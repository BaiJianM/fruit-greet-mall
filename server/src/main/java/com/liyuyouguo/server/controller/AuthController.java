package com.liyuyouguo.server.controller;

import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.dto.shop.UserLoginDto;
import com.liyuyouguo.common.beans.vo.UserLoginInfoVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import com.liyuyouguo.common.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 验证控制层
 *
 * @author baijianmin
 */
@Slf4j
@FruitGreetController("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 微信登录
     *
     * @param dto 小程序登录传参
     * @return UserLoginInfoVo 用户信息
     */
    @PostMapping("/loginByWeixin")
    public FruitGreetResponse<UserLoginInfoVo> loginByWeChat(@RequestBody UserLoginDto dto) {
        return FruitGreetResponse.success(userService.loginByWeChat(dto.getCode()));
    }

}
