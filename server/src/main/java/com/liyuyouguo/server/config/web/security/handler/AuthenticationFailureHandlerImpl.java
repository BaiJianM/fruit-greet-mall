package com.liyuyouguo.server.config.web.security.handler;

import com.liyuyouguo.server.commons.SystemError;
import com.liyuyouguo.server.utils.SecurityRenderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * 登录失败处理类
 *
 * @author baijianmin
 */
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String msg;
        if (StringUtils.isBlank(username)) {
            msg = SystemError.USER_NAME_NOT_NULL.getDescribe();
        } else if (StringUtils.isBlank(password)) {
            msg = SystemError.PASSWORD_NOT_NULL.getDescribe();
        } else {
            msg = SystemError.LOGIN_FAIL.getDescribe();
        }
        SecurityRenderUtil.renderErrorResponse(response, HttpStatus.BAD_REQUEST, msg);
    }
}
