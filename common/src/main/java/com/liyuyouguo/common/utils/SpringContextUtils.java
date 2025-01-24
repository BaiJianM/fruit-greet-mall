package com.liyuyouguo.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * spring上下文工具
 *
 * @author baijianmin
 */
@Slf4j
@Component
public class SpringContextUtils implements ApplicationContextAware, DisposableBean {

    /**
     * -- GETTER --
     *  获取应用程序上下文
     *
     * @return ApplicationContext 应用程序上下文
     */
    @Getter
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        synchronized (SpringContextUtils.class) {
            if (SpringContextUtils.applicationContext == null) {
                SpringContextUtils.applicationContext = applicationContext;
            }
        }
    }

    @Override
    public void destroy() {
        clear();
    }

    /**
     * 清空当前应用上下文
     */
    public static void clear() {
        applicationContext = null;
    }

    /**
     * 通过类名称获取bean实例
     *
     * @param name 类名称
     * @return bean实例
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过类对象获取bean实例
     *
     * @param clazz 类对象
     * @return T bean实例
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 同时通过类名称和类对象获取bean实例
     *
     * @param name  类名称
     * @param clazz 类对象
     * @return T bean实例
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 获取当前请求对象
     *
     * @return HttpServletRequest 当前请求对象
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
    }

    /**
     * 获取当前响应对象
     *
     * @return HttpServletResponse 当前响应对象
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getResponse();
    }
}
