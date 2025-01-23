package com.liyuyouguo.server.utils;

import com.liyuyouguo.server.beans.UserInfo;
import org.springframework.core.NamedThreadLocal;

/**
 * 获取当前用户信息
 *
 * @author baijianmin
 */
public class UserInfoUtils {

    /**
     * 当前线程存储登录用户信息
     */
    private static final ThreadLocal<UserInfo> USER_LOCAL = new NamedThreadLocal<>("ThreadLocal UserInfoUtils");

    /**
     * 获取当前登录用户信息
     *
     * @return UserInfo 用户信息
     */
    public static UserInfo getUserInfo() {
        return USER_LOCAL.get() == null ? new UserInfo() : USER_LOCAL.get();
    }

    /**
     * 设置当前登录用户信息
     *
     * @param vo 用户信息
     */
    public static void setUserInfo(UserInfo vo) {
        USER_LOCAL.set(vo);
    }

    /**
     * 清除当前登录用户信息（防止内存泄露）
     */
    public static void clearUserInfo() {
        USER_LOCAL.remove();
    }

    /**
     * 获取登录用户id
     *
     * @return Long 用户id
     */
    public static Long getUserId() {
        return getUserInfo().getUserId();
    }
}
