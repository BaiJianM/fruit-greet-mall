package com.liyuyouguo.common.commons;

import java.io.Serial;

/**
 * 全局自定义异常类
 *
 * @author baijianmin
 */
public class FruitGreetException extends RuntimeException implements ErrorResponse<Integer> {

    @Serial
    private static final long serialVersionUID = -2015887150228289536L;

    /**
     * 错误码
     */
    private final Integer code;
    /**
     * 错误描述
     */
    private final String describe;

    public FruitGreetException(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public FruitGreetException(String describe) {
        this.code = SystemError.FAIL.getCode();
        this.describe = describe;
    }

    public FruitGreetException(ErrorResponse<Integer> errorResponse) {
        this.code = errorResponse.getCode();
        this.describe = errorResponse.getDescribe();
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDescribe() {
        return describe;
    }
}
