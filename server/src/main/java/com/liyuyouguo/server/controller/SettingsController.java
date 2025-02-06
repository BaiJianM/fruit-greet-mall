package com.liyuyouguo.server.controller;

import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.dto.shop.SettingsSaveDto;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import com.liyuyouguo.common.entity.shop.ShowSettings;
import com.liyuyouguo.common.entity.shop.User;
import com.liyuyouguo.common.service.UserService;
import com.liyuyouguo.server.service.SettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 显示配置控制层
 *
 * @author baijianmin
 */
@Slf4j
@FruitGreetController("/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final SettingsService settingsService;

    private final UserService userService;

    /**
     * 获取显示配置
     *
     * @return ShowSettings 首页显示配置实体类
     */
    @GetMapping("/showSettings")
    public FruitGreetResponse<ShowSettings> showSettings() {
        return FruitGreetResponse.success(settingsService.getShowSettings());
    }

    /**
     * 获取登录用户信息
     *
     * @return User 登录用户信息
     */
    @GetMapping("/userDetail")
    public FruitGreetResponse<User> getUserDetail() {
        return FruitGreetResponse.success(userService.getUserDetail());
    }

    @PostMapping("/save")
    public FruitGreetResponse<Integer> save(@RequestBody SettingsSaveDto dto) {
        return FruitGreetResponse.success(userService.save(dto));
    }

}
