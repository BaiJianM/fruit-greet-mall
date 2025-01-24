package com.liyuyouguo.common.config.operatelog.function;

import com.liyuyouguo.common.annotations.operatelog.LogFunction;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册自定义函数
 *
 * @author baijianmin
 */
@Data
@Slf4j
@Component
public class OperateLogFunctionRegistrar implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static Map<String, Method> functionMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        Map<String, Object> beanWithAnnotation = applicationContext.getBeansWithAnnotation(LogFunction.class);
        beanWithAnnotation.values()
                .forEach(
                        component -> {
                            Method[] methods = component.getClass().getMethods();
                            LogFunction classFunc = component.getClass().getAnnotation(LogFunction.class);
                            String prefixName = classFunc.value();
                            if (StringUtils.hasText(prefixName)) {
                                prefixName += "_";
                            }
                            for (Method method : methods) {
                                if (method.isAnnotationPresent(LogFunction.class) && isStaticMethod(method)) {
                                    LogFunction func = method.getAnnotation(LogFunction.class);
                                    String registerName =
                                            StringUtils.hasText(func.value()) ? func.value() : method.getName();
                                    functionMap.put(prefixName + registerName, method);
                                    log.info("LogRecord register custom function [{}] as name [{}]",
                                            method, prefixName + registerName);
                                }
                            }
                        }
                );
    }

    /**
     * 注册spel解析函数
     *
     * @param context spel上下文
     */
    public static void register(StandardEvaluationContext context) {
        functionMap.forEach(context::registerFunction);
    }

    /**
     * 判断是否为静态方法
     *
     * @param method 目标方法
     * @return boolean 是否静态方法
     */
    private static boolean isStaticMethod(Method method) {
        if (method == null) {
            return false;
        }
        int modifiers = method.getModifiers();
        return Modifier.isStatic(modifiers);
    }
}
