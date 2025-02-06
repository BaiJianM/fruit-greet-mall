package com.liyuyouguo.server.controller;

import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.vo.AppInfoVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import com.liyuyouguo.server.service.IndexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页控制层
 *
 * @author baijianmin
 */
@Slf4j
@FruitGreetController("/index")
@RequiredArgsConstructor
public class IndexController {

    private final IndexService indexService;

    /**
     * 获取小程序信息
     *
     * @return ShowSettings 首页显示配置实体类
     */
    @GetMapping("/appInfo")
    public FruitGreetResponse<AppInfoVo> getAppInfo() {
        return FruitGreetResponse.success(indexService.getAppInfo());
    }



}
