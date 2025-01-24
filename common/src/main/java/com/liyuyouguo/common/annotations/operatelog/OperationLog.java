package com.liyuyouguo.common.annotations.operatelog;

import java.lang.annotation.*;

/**
 * 操作日志
 *
 * @author baijianmin
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(OperationLogs.class)
public @interface OperationLog {

    /**
     * 操作事件
     * 必填
     * SpEL表达式
     */
    String event() default "";

    /**
     * 日志内容
     * 可选
     * SpEL表达式
     */
    String msg() default "";

    /**
     * 额外信息
     * 可选
     * SpEL表达式
     */
    String extra() default "";

    /**
     * 操作人ID
     * 可选
     * SpEL表达式
     */
    String operatorId() default "";

    /**
     * 操作人姓名
     * 可选
     * SpEL表达式
     */
    String operatorName() default "";

    /**
     * 切面执行时机
     * true: 执行方法前解析切面逻辑
     * false: 执行方法后解析切面逻辑
     */
    boolean executeBeforeFunc() default false;

    /**
     * 是否记录返回值
     * true: 记录返回值
     * false: 不记录返回值
     */
    boolean recordReturnValue() default false;

    /**
     * 日志记录条件
     * 可选
     * SpEL表达式
     */
    String condition() default "'true'";

    /**
     * 自定义方法执行是否成功 用于根据返回体或其他情况下自定义日志实体中的success字段
     * 可选
     * SpEL表达式
     */
    String success() default "";
}
