package com.liyuyouguo.common.config.web.security.handler;

import com.liyuyouguo.common.beans.UserInfo;
import com.liyuyouguo.common.service.security.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * 登录成功处理类
 *
 * @author baijianmin
 */
@Slf4j
@RequiredArgsConstructor
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final AuthenticationService authenticationService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();
        authenticationService.login(request, response, userInfo);
    }
}
