package com.liyuyouguo.server.commons;

/**
 * 常量属性类接口类
 *
 * @author baijianmin
 */
public interface Constants {

    /**
     * 系统常量
     */
    interface SystemInfo {
        /**
         * jwt鉴权前缀
         */
        String AUTHORIZATION_PREFIX = "Bearer ";

        /**
         * 登录接口url
         */
        String LOGIN_URL = "/login";

        /**
         * 请求头鉴权键
         */
        String AUTHORIZATION = "Authorization";

        /**
         * 超级管理员
         */
        String ADMIN = "admin";

        /**
         * 文件上传路径
         */
        String UPLOAD_PATH = "/";
    }

    /**
     * 文件常量
     */
    interface FileInfo {
        /**
         * 分隔符
         */
        String COMMA = ",";
    }

    /**
     * 用户登录信息缓存常量
     */
    interface LoginUser {
        /**
         * 用户登录信息缓存前缀
         */
        String LOGIN_USER_PREFIX = "sys_user_info:";

        /**
         * 用户信息本地线程存储key
         */
        String USER_INFO = "USER_INFO";
    }

    /**
     * 数据字典缓存相关
     */
    interface DictionaryCache {
        /**
         * 所有字典的缓存key
         */
        String ALL_DICT_CACHE = "all_dict_cache";

        /**
         * 字典缓存锁
         */
        String DICT_LOCK = "dict_lock";
    }

    /**
     * 验证码缓存相关
     */
    interface AuthCode {
        /**
         * 验证码缓存key
         */
        String CAPTCHA_CODE_KEY = "auth_code:";
    }

    /**
     * 日志事件
     */
    interface LogEvent {
        /**
         * 登录登出事件
         */
        String LOGIN_EVENT = "登录登出事件";
    }

}
