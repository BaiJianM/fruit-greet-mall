package com.liyuyouguo.common.config.web;

import com.liyuyouguo.common.commons.FruitShopException;
import com.liyuyouguo.common.commons.FruitShopResponse;
import com.liyuyouguo.common.commons.SystemError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 全局异常统一拦截类
 *
 * @author baijianmin
 */
@Slf4j
@RestControllerAdvice
public class CommonExtHandler {

    /**
     * 捕获全局异常, 处理所有不可知的异常
     *
     * @param e 未知异常类型
     * @return AnswerResponse<T> 自定义响应对象
     */
    @ExceptionHandler(value = Exception.class)
    public <T> FruitShopResponse<T> handleException(Exception e) {
        log.error("Exception异常: ", e);
        return FruitShopResponse.fail();
    }

    /**
     * 前后端参数不匹配校验（json解析异常）
     *
     * @param e json异常类型
     * @return AnswerResponse<SystemError> 自定义响应对象
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public FruitShopResponse<SystemError> validJson(HttpMessageNotReadableException e) {
        log.info("Json异常: ", e);
        return FruitShopResponse.fail(SystemError.ILLEGAL_JSON, "", HttpStatus.BAD_REQUEST);
    }

    /**
     * 接口请求方式不匹配异常
     *
     * @param e http请求方式异常
     * @return AnswerResponse<String> 自定义响应对象
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public FruitShopResponse<String> validHttpMethod(HttpRequestMethodNotSupportedException e) {
        log.info("请求方法不匹配: ", e);
        String format = String.format("该接口支持的请求方法为 [%s]", Objects.requireNonNull(e.getSupportedMethods())[0]);
        return FruitShopResponse.fail(format, SystemError.NOT_SUPPORTED_METHOD, HttpStatus.BAD_REQUEST);
    }

    /**
     * 接口请求参数缺失
     *
     * @param e 参数缺失异常
     * @return AnswerResponse<String> 自定义响应对象
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public FruitShopResponse<String> validRequestParamMissing(MissingServletRequestParameterException e) {
        // 参数名
        String parameterName = e.getParameterName();
        // 参数类型
        String parameterType = e.getParameterType();
        log.info("请求参数缺失，参数名[{}]，参数类型[{}]", parameterName, parameterType);
        String msg = "参数名[%s]，参数类型[%s]";
        String format = String.format(msg, parameterName, parameterType);
        return FruitShopResponse.fail(format, SystemError.PARAMETER_MISSING, HttpStatus.BAD_REQUEST);
    }

    /**
     * controller方法中，对象作为参数的校验
     *
     * @param e 对象参数异常
     * @return AnswerResponse<?> 自定义响应对象
     */
    @SuppressWarnings("all")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public FruitShopResponse<?> validEntity(MethodArgumentNotValidException e) {
        Map<String, String> collect = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        String msg = String.join(",", collect.values());
        return FruitShopResponse.fail(msg, SystemError.PARAMETER_ABNORMALITY, HttpStatus.BAD_REQUEST);
    }

    /**
     * controller方法中：1、对象接收表单数据的校验；2、对象接收表单数据并做分组验参数据的校验
     *
     * @param e 表单参数异常
     * @return AnswerResponse<String> 自定义响应对象
     */
    @ExceptionHandler(BindException.class)
    public FruitShopResponse<String> exceptionHandler(BindException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        Map<String, String> collect = new HashMap<>(16);
        allErrors.forEach(error -> {
            FieldError fieldError = (FieldError) error;
            collect.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        String msg = String.join(",", collect.values());
        return FruitShopResponse.fail(msg, SystemError.PARAMETER_ABNORMALITY, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理自定义异常类
     *
     * @param e 自定义异常
     * @return AnswerResponse<String> 自定义响应对象
     */
    @ExceptionHandler(value = FruitShopException.class)
    public FruitShopResponse<String> handleAnswerException(FruitShopException e) {
        log.error("AnswerException异常: {}", e.getDescribe());
        return FruitShopResponse.warn(null, e);
    }
}
