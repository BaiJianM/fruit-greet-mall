package com.liyuyouguo.server.service.shop;

import com.liyuyouguo.server.entity.shop.ShowSettings;
import com.liyuyouguo.server.mapper.ShowSettingsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 配置服务类
 *
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SettingsService {

    private final ShowSettingsMapper showSettingsMapper;

    /**
     * 获取首页显示配置
     *
     * @return ShowSettings 首页显示配置实体类
     */
    public ShowSettings getShowSettings() {
        return showSettingsMapper.selectById(1);
    }
}
