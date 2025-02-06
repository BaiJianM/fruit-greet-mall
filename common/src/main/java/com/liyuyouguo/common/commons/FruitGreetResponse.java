package com.liyuyouguo.common.commons;

import com.alibaba.fastjson2.JSON;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

/**
 * 自定义接口请求响应
 *
 * @author baijianmin
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class FruitGreetResponse<T> extends ResponseEntity<FruitGreetResponse.ResponseBody<T>> {

    /**
     * 因bug产生的未知异常提示
     */
    private static final String ERROR_DESCRIPTION = "服务器异常";

    /**
     * 默认请求成功提示
     */
    private static final String SUCCESS_DESCRIPTION = "请求成功";

    /**
     * 默认请求成功状态码
     */
    private static final Integer SUCCESS_CODE = 200;

    /**
     * 默认请求失败状态码
     */
    private static final Integer SERVER_ERROR_CODE = 500;

    /**
     * 自定义响应体
     */
    private final ResponseBody<T> body;

    /**
     * 自定义业务状态码请求头key
     */
    public static final String BUSINESS_CODE_HEADER = "businessCode";


    public FruitGreetResponse(@NonNull ResponseBody<T> body, HttpStatus status) {
        super(body, status);
        this.body = body;
    }

    /**
     * 自定义http响应体构造器
     *
     * @param data   返回数据
     * @param msg    描述
     * @param code   业务状态码
     * @param status http状态码
     */
    public FruitGreetResponse(T data, String msg, Integer code, HttpStatus status) {
        super(new ResponseBody<>(data, msg, code), status);
        this.body = new ResponseBody<>(data, msg, code);
    }

    public FruitGreetResponse(@NonNull ResponseBody<T> body, @Nullable MultiValueMap<String, String> headers,
                              HttpStatus status) {
        super(body, headers, status);
        this.body = body;
    }

    public static <T> FruitGreetResponse<T> success() {
        return new FruitGreetResponse<>(new ResponseBody<>(null, SUCCESS_DESCRIPTION, SUCCESS_CODE), HttpStatus.OK);
    }

    public static <T> FruitGreetResponse<T> success(T data) {
        return new FruitGreetResponse<>(new ResponseBody<>(data, SUCCESS_DESCRIPTION, SUCCESS_CODE), HttpStatus.OK);
    }

    public static <T> FruitGreetResponse<T> success(T data, HttpStatus status) {
        return new FruitGreetResponse<>(new ResponseBody<>(data, SUCCESS_DESCRIPTION, status.value()), status);
    }

    public static <T> FruitGreetResponse<T> success(T data, String msg, HttpStatus status) {
        return new FruitGreetResponse<>(new ResponseBody<>(data, msg, status.value()), status);
    }

    public static <T> FruitGreetResponse<T> warn(T data, ErrorResponse<Integer> error) {
        return new FruitGreetResponse<>(new ResponseBody<>(data, error.getDescribe(), error.getCode()), HttpStatus.BAD_REQUEST);
    }

    public static <T> FruitGreetResponse<T> fail() {
        return new FruitGreetResponse<>(new ResponseBody<>(null, ERROR_DESCRIPTION, SERVER_ERROR_CODE),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> FruitGreetResponse<T> fail(T data) {
        return new FruitGreetResponse<>(new ResponseBody<>(data, ERROR_DESCRIPTION, SERVER_ERROR_CODE),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> FruitGreetResponse<T> fail(String msg) {
        return new FruitGreetResponse<>(new ResponseBody<>(null, msg, SERVER_ERROR_CODE),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> FruitGreetResponse<T> fail(T data, String msg) {
        return new FruitGreetResponse<>(new ResponseBody<>(data, msg, SERVER_ERROR_CODE), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> FruitGreetResponse<T> fail(T data, String msg, HttpStatus status) {
        return new FruitGreetResponse<>(new ResponseBody<>(data, msg, status.value()), status);
    }

    public static <T> FruitGreetResponse<T> fail(T data, ErrorResponse<Integer> error) {
        return new FruitGreetResponse<>(new ResponseBody<>(data, error.getDescribe(), SERVER_ERROR_CODE),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> FruitGreetResponse<T> fail(T data, ErrorResponse<Integer> error, HttpStatus status) {
        return new FruitGreetResponse<>(new ResponseBody<>(data, error.getDescribe(), status.value()), status);
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @NonNull
    @Override
    public ResponseBody<T> getBody() {
        return this.body;
    }

    /**
     * 自定义响应体
     *
     * @author baijianmin
     */
    @Schema(description = "响应体")
    @Accessors(chain = true)
    public record ResponseBody<T>(@Schema(description = "响应数据内容") T data,
                                  @Schema(description = "描述信息") String msg,
                                  @Schema(description = "状态码（含自定义业务异常码与http状态码）") Integer code) {
        @Override
        public String toString() {
            return JSON.toJSONString(new ResponseBody<>(data, msg, code));
        }
    }
}
