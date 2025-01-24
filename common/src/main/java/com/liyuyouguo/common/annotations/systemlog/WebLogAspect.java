package com.liyuyouguo.common.annotations.systemlog;

import com.alibaba.fastjson2.JSONObject;
import com.liyuyouguo.common.commons.FruitShopException;
import com.liyuyouguo.common.commons.FruitShopResponse;
import com.liyuyouguo.common.utils.FruitShopUtils;
import com.liyuyouguo.common.utils.IpUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Web日志切面
 *
 * @author baijianmin
 */
@Slf4j
@Aspect
@Component
public class WebLogAspect {

    /**
     * Web日志环绕通知
     *
     * @param pjp 切点信息
     * @return Object 原操作执行结果
     * @throws Throwable 异常对象
     */
    @Around("within(com.liyuyouguo.*.controller..*)" +
            "|| @annotation(com.liyuyouguo.common.annotations.systemlog.WebLog)")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 获取切面方法
        Method method = FruitShopUtils.getMethod(pjp);
        // 获取web日志注解
        WebLog webLog = method.getDeclaredAnnotation(WebLog.class);
        // 如果关闭了web日志输出，就直接返回
        if (webLog != null && webLog.disable()) {
            return pjp.proceed();
        }
        // 格式化日志
        JSONObject paramsLogJson = formatLog(method);
        Object result = null;
        // 执行原方法
        try {
            result = pjp.proceed();
            // 只有开启了debug日志，才会输出接口返回值
            if (!log.isDebugEnabled()) {
                return result;
            }
        } catch (Exception e) {
            try {
                String errMsg = e.getMessage();
                if (e instanceof FruitShopException fruitShopException) {
                    errMsg = fruitShopException.getDescribe();
                }
                paramsLogJson.put("Web接口异常", errMsg);
            } catch (Exception ex) {
                log.error("Web接口异常日志转换失败，错误信息: {}", ex.getMessage());
            }
            throw e;
        } finally {
            try {
                if (result instanceof FruitShopResponse<?> response) {
                    paramsLogJson.put("请求返回", response.getBody());
                } else {
                    paramsLogJson.put("请求返回", result);
                }
                log.info("Web接口日志: " + paramsLogJson);
            } catch (Exception e) {
                log.error("Web接口日志记录失败，错误信息: {}", e.getMessage());
            }
        }
        return result;
    }

    /**
     * 格式化日志
     *
     * @param method 原方法
     * @return JSONObject 格式化日志结果
     * @throws IOException 输入输出流异常
     */
    private static JSONObject formatLog(Method method) throws IOException {
        // 日志标题
        String title;
        // 尝试获取Swagger注解
        if (method.isAnnotationPresent(Operation.class)) {
            Operation operation = method.getDeclaredAnnotation(Operation.class);
            title = StringUtils.isBlank(operation.summary()) ? operation.description() : operation.summary();
        } else {
            title = method.getName();
        }
        // 获取请求入参
        ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert ra != null;
        HttpServletRequest request = ra.getRequest();
        JSONObject paramsLogJson = new JSONObject();
        paramsLogJson.put("标题", title);
        paramsLogJson.put("请求资源", request.getRequestURI());
        paramsLogJson.put("请求方式", request.getMethod());
        paramsLogJson.put("请求IP", IpUtils.getIpAddress(request));
        JSONObject inputJson = new JSONObject();
        inputJson.put("uri", request.getParameterMap());
        inputJson.put("body", FruitShopUtils.getRequestBody(request));
        paramsLogJson.put("请求入参", inputJson);
        return paramsLogJson;
    }

}
