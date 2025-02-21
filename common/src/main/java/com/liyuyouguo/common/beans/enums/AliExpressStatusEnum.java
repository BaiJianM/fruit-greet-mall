package com.liyuyouguo.common.beans.enums;

import lombok.Getter;

/**
 * 阿里云快递查询状态枚举
 *
 * @author baijianmin
 */
@Getter
public enum AliExpressStatusEnum {

    NORMAL("0", "正常查询"),
    EXPRESS_ERROR("201", "快递单号错误"),
    EXPRESS_COMPANY_NOT_FOUND("203", "快递公司不存在"),
    EXPRESS_COMPANY_CANNOT_READ("204", "快递公司识别失败"),
    NO_INFO("205", "没有信息"),
    EXPRESS_NO_LIMITATION("207", "该单号被限制，错误单号"),
    ;

    private final String code;

    private final String desc;

    AliExpressStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据快递状态code获取枚举
     *
     * @param code 快递状态code
     * @return AliExpressStatusEnum 阿里云快递查询状态枚举
     * @throws IllegalArgumentException 如果未找到对应的枚举，则抛出此异常
     */
    public static AliExpressStatusEnum fromCode(String code) {
        for (AliExpressStatusEnum type : AliExpressStatusEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的阿里云快递查询状态code: " + code);
    }

}
