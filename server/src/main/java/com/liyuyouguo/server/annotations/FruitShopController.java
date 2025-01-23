package com.liyuyouguo.server.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * 组合式Web控制层注解
 *
 * @author baijianmin
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@RequestMapping
public @interface FruitShopController {
    /**
     * 统一请求路径
     *
     * @return String 路径
     */
    @AliasFor("path")
    String value() default "";

    /**
     * 统一请求路径
     *
     * @return String 路径
     */
    @AliasFor("value")
    String path() default "";
}
