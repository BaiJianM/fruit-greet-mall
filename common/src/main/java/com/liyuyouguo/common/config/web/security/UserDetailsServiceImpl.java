package com.liyuyouguo.common.config.web.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liyuyouguo.common.commons.SystemError;
import com.liyuyouguo.common.entity.shop.Admin;
import com.liyuyouguo.common.mapper.AdminMapper;
import com.liyuyouguo.common.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.liyuyouguo.common.beans.UserInfo;

/**
 * spring security鉴权用户信息实现
 *
 * @author baijianmin
 */
@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdminMapper adminMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 获取用户信息
        LambdaQueryWrapper<Admin> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(Admin::getUsername, username);
        userWrapper.eq(Admin::getIsDelete, false);
        Admin sysUser = adminMapper.selectOne(userWrapper);
        if (sysUser == null) {
            throw new UsernameNotFoundException(SystemError.USER_NOT_EXIST.getDescribe());
        }
        return ConvertUtils.convert(sysUser, UserInfo::new, (s, t) -> {
                    t.setUserId(s.getId());
                    t.setIsEnable(true);
                })
                .orElseThrow(() -> new UsernameNotFoundException(SystemError.USER_NOT_EXIST.getDescribe()));
    }
}
