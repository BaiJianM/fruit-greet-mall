package com.liyuyouguo.server.annotations.systemlog;

import java.lang.annotation.*;

/**
 * Web日志注解
 *
 * @author baijianmin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebLog {

    /**
     * 日志标题
     */
    String value() default "";

    /**
     * 是否禁用日志输出: true(禁用);false(启用)
     */
    boolean disable() default false;

}
