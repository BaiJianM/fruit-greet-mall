package com.liyuyouguo.common.annotations.operatelog;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志记录
 *
 * @author baijianmin
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogDiff {

    /**
     * 类/字段的别名 不填则默认类/字段名
     */
    String alias() default "";
}
