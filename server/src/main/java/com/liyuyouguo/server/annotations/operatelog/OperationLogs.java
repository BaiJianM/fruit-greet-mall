package com.liyuyouguo.server.annotations.operatelog;

import java.lang.annotation.*;

/**
 * 操作日志
 *
 * @author baijianmin
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLogs {

    /**
     * 多组操作日志注解
     */
    OperationLog[] value();
}
