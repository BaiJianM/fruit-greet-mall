package com.liyuyouguo.server.commons;

/**
 * 系统异常枚举类
 *
 * @author baijianmin
 */
public enum SystemError implements ErrorResponse<Integer> {

    // 前端控制器响应成功
    SUCCESS(0, "success"),

    // 前端控制器响应失败
    FAIL(-1, "fail"),

    // 服务端未知异常
    SERVER_EXCEPTION(3001, "服务端异常"),
    CONNECTION_TIMEOUT(3002, "连接超时"),
    SERVICE_UNAVAILABLE(3003, "服务不可用"),

    // 接口传递参数异常
    PARAMETER_ABNORMALITY(4001, "参数异常，请查验swagger接口参数"),
    PARAMETER_MISSING(4002, "参数缺失，请查验swagger接口参数"),
    ILLEGAL_JSON(4003, "非法的JSON格式，请查验swagger接口参数格式"),
    NOT_SUPPORTED_METHOD(4004, "请求方法不匹配"),

    // 文件类型异常
    FILE_NOT_EXIST(5001, "文件不存在"),
    FILE_UPLOAD_NOT_EXIST(5002, "上传文件不存在或为空"),
    FILE_DELETE_NOT_EXIST(5003, "删除文件不存在"),
    ILLEGAL_FILE_TYPE(5004, "文件类型异常，不受支持的文件类型"),
    WEBSOCKET_FILE_ARGUMENT_MISSING(5005, "缺少请求头中的WebSocket连接标识信息，请查阅接口文档"),
    FILE_CREATE_ERROR(5006, "文件创建异常"),

    // 请求资源类型异常
    DATA_NOT_EXIST(6001, "数据不存在"),
    LOGIN_FIRST(6002, "请先登录"),
    TOKEN_FAIL(6003, "请求头无Authorization或Authorization值错误"),
    TOKEN_WRONG(6004, "令牌错误或已过期"),
    AUTHORIZATION_SERVER_BROKEN(6005, "授权服务异常"),
    UNAUTHORIZED(6006, "没有访问该资源的权限"),
    USER_NAME_NOT_NULL(6008, "账号不能为空"),
    PASSWORD_NOT_NULL(6009, "密码不能为空"),
    LOGIN_FAIL(6010, "用户名或密码错误"),
    DATA_ACCESS_DENIED(6011, "用户暂无该数据权限"),
    ACCOUNT_NOT_EXIST(6012, "账号不存在"),
    USER_NOT_EXIST(6013, "用户不存在"),
    AUTH_CODE_CREATE_ERROR(6014, "图片验证码生成异常"),
    AUTH_CODE_ERROR(6015, "验证码错误"),
    OLD_PASSWORD_NOT_MATCH_ERROR(6016, "原密码错误"),
    ROLE_CREATE_ERROR(6017, "角色创建出错"),
    ROLE_HAS_USER(6018, "角色存在关联用户，不允许删除"),
    PARENT_NOT_EXIT(6019, "父节点不存在"),
    PERMISSION_CREATE_ERROR(6020, "权限创建出错"),
    PERMISSION_UPDATE_ERROR(6021, "权限更新出错"),
    PERMISSION_HAS_ROLE(6022, "权限存在关联角色，不允许删除"),
    API_NOT_FOUND(6023, "请求的资源不存在"),
    IS_ADMIN_ERROR(6024, "非管理员不可登录此系统，如有疑问，请联系相关人员"),


    // 系统运行异常
    MESSAGE_BUILD_ERROR(8001, "消息体为空或缺少业务标识"),
    ;

    /**
     * 异常状态码
     */
    private final Integer code;

    /**
     * 异常描述
     */
    private final String describe;

    SystemError(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDescribe() {
        return describe;
    }


}
