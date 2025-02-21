package com.liyuyouguo.common.beans.enums;

import lombok.Getter;

/**
 * 阿里云快递揽件状态
 *
 * @author baijianmin
 */
@Getter
public enum AliExpressDeliveryStatusEnum {

    PICKUP("0", "快递收件(揽件)"),

    IN_TRANSIT("1", "在途中"),

    OUT_FOR_DELIVERY("2", "正在派件"),

    DELIVERED("3", "已签收"),

    DELIVERY_FAILED("4", "派送失败"),

    EXCEPTION("5", "疑难件"),

    RETURNED("6", "退件签收");

    private final String code;

    private final String desc;

    AliExpressDeliveryStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据快递揽件状态code获取枚举
     *
     * @param code 快递状态code
     * @return AliExpressDeliveryStatusEnum 阿里云快递查询揽件状态枚举
     * @throws IllegalArgumentException 如果未找到对应的枚举，则抛出此异常
     */
    public static AliExpressDeliveryStatusEnum fromCode(String code) {
        for (AliExpressDeliveryStatusEnum type : AliExpressDeliveryStatusEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的阿里云快递查询揽件状态code: " + code);
    }

}
