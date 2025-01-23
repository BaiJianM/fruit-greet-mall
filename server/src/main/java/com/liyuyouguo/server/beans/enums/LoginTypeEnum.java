package com.liyuyouguo.server.beans.enums;

/**
 * 登录类型枚举类
 *
 * @author baijianmin
 */
public enum LoginTypeEnum {
    /**
     * 登录
     */
    LOGIN("100001", "登录"),
    /**
     * 退出
     */
    LOGOUT("100002", "退出"),
    ;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private final String code;

    private final String name;

    LoginTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(String code) {
        for (LoginTypeEnum value : LoginTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return "";
    }

}
