package com.liyuyouguo.server.annotations.operatelog;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志记录函数
 *
 * @author baijianmin
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface LogFunction {

    /**
     * 自定义函数的别名，如果为空即使用函数名
     */
    String value() default "";
}
