package com.liyuyouguo.common.utils;

import com.alibaba.fastjson2.JSON;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import com.liyuyouguo.common.commons.SystemError;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * spring security渲染响应工具
 *
 * @author baijianmin
 */
@Slf4j
public class SecurityRenderUtil {

    private static final String CONTENT_TYPE = "application/json;charset=GBK";

    private SecurityRenderUtil() {}

    /**
     * 渲染失败响应结果
     *
     * @param response 响应对象
     * @param status   http状态码
     * @param msg      响应信息
     * @throws IOException 输入输出流异常
     */
    public static void renderErrorResponse(HttpServletResponse response, HttpStatus status, String msg) throws IOException {
        response.setStatus(status.value());
        response.setContentType(CONTENT_TYPE);
        FruitGreetResponse<String> fail = FruitGreetResponse.fail("", msg, status);
        response.getWriter().write(JSON.toJSONString(fail.getBody()));
    }

    /**
     * 渲染Token错误响应结果
     *
     * @param response 响应对象
     * @throws IOException 输入输出流异常
     */
    public static void renderTokenErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType(CONTENT_TYPE);
        FruitGreetResponse<String> fail = FruitGreetResponse.fail("", SystemError.TOKEN_WRONG);
        response.getWriter().write(JSON.toJSONString(fail.getBody()));
    }

    /**
     * 渲染成功响应结果
     *
     * @param response 响应对象
     * @param msg      响应信息
     * @param data     响应数据
     * @throws IOException 输入输出流异常
     */
    public static void renderSuccessResponse(HttpServletResponse response, String msg, Object data) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(CONTENT_TYPE);
        FruitGreetResponse<?> success = FruitGreetResponse.success(data, msg, HttpStatus.OK);
        response.getWriter().write(JSON.toJSONString(success.getBody()));
    }

}
