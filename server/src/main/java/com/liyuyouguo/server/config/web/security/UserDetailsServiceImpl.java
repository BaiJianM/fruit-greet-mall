package com.liyuyouguo.server.config.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * spring security鉴权用户信息实现
 *
 * @author baijianmin
 */
@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 获取用户信息
//        LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
//        userWrapper.eq(SysUser::getUsername, username);
//        userWrapper.eq(SysUser::getIsDelete, false);
//        SysUser sysUser = userMapper.selectOne(userWrapper);
//        if (sysUser == null) {
//            throw new UsernameNotFoundException(SystemError.USER_NOT_EXIST.getDescribe());
//        }
//        return ConvertUtils.convert(sysUser, UserInfo::new, (s, t) -> t.setUserId(s.getId()))
//                .orElseThrow(() -> new UsernameNotFoundException(SystemError.USER_NOT_EXIST.getDescribe()));
        return null;
    }
}
