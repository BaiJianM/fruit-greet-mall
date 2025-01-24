package com.liyuyouguo.common.service.security;

import com.alibaba.fastjson2.JSON;
import com.liyuyouguo.common.beans.UserInfo;
import com.liyuyouguo.common.commons.Constants;
import com.liyuyouguo.common.commons.SystemError;
import com.liyuyouguo.common.config.web.security.jwt.JwtProperties;
import com.liyuyouguo.common.config.web.security.jwt.JwtService;
import com.liyuyouguo.common.entity.SysOperateLog;
import com.liyuyouguo.common.service.CaptchaService;
import com.liyuyouguo.common.service.UserService;
import com.liyuyouguo.common.utils.RedisUtils;
import com.liyuyouguo.common.utils.SecurityRenderUtil;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 完善登录鉴权信息服务类
 *
 * @author baijianmin
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;

    private final RedisUtils redisUtils;

    private final JwtProperties jwtProperties;

    private final CaptchaService captchaService;

    private final UserService userService;

    public void login(HttpServletRequest request, HttpServletResponse response,
                      UserInfo userInfo) throws IOException {
        String authCode = request.getParameter("code");
        if (StringUtils.isNotBlank(authCode)) {
            String uuid = request.getParameter("uuid");
            // 先校验验证码是否正确
            boolean verify = captchaService.verify(authCode, uuid);
            if (!verify) {
                SecurityRenderUtil.renderErrorResponse(response,
                        HttpStatus.BAD_REQUEST, SystemError.AUTH_CODE_ERROR.getDescribe());
                return;
            }
        }
        // 如果是web管理端登录，只有管理员账号可以登
        Boolean isWeb = Boolean.valueOf(request.getParameter("isWeb"));
        if (Boolean.TRUE.equals(isWeb) && Boolean.TRUE.equals(!userInfo.getIsAdmin())) {
            SecurityRenderUtil.renderErrorResponse(response,
                    HttpStatus.BAD_REQUEST, SystemError.IS_ADMIN_ERROR.getDescribe());
            return;
        }
        // 返回的用户信息去掉密码
        userInfo.cleanPassword();
        String token = jwtService.generateToken(userInfo.getUsername());
        userInfo.setToken(token);
        // 如果是web管理端用户，则获取用户关联角色、权限信息
//        if (Boolean.TRUE.equals(isWeb)) userService.setRoleAndPermission(userInfo);
        // 缓存token关联的用户信息
        String key = Constants.LoginUser.LOGIN_USER_PREFIX + userInfo.getUsername();
        redisUtils.setEx(key, JSON.toJSONString(userInfo), jwtProperties.getExpire(), TimeUnit.SECONDS);
        SecurityRenderUtil.renderSuccessResponse(response, "请求成功", userInfo);
    }

    private void createLoginLog(HttpServletRequest request) {
        // 客户端信息
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 登录日志记录
        SysOperateLog sysOperateLog = new SysOperateLog();
        sysOperateLog.setOs(userAgent.getOperatingSystem().getName());
        sysOperateLog.setBrowser(userAgent.getBrowser().getName());
//        logMapper.insert();
    }
}
