package com.liyuyouguo.server.commons;

/**
 * 响应异常接口类
 *
 * @author baijianmin
 */
public interface ErrorResponse<T> {

    /**
     * 获取异常对象
     *
     * @return T 异常对象
     */
    T getCode();

    /**
     * 获取错误描述
     *
     * @return String 错误描述
     */
    String getDescribe();

}
