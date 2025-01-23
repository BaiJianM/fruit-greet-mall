package com.liyuyouguo.server.config.web.security.handler;

import com.liyuyouguo.server.commons.Constants;
import com.liyuyouguo.server.config.web.security.jwt.JwtService;
import com.liyuyouguo.server.utils.RedisUtils;
import com.liyuyouguo.server.utils.SecurityRenderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

/**
 * 登出成功处理类
 *
 * @author baijianmin
 */
@Slf4j
@RequiredArgsConstructor
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    private final RedisUtils redisUtils;

    private final JwtService jwtService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {
        // 获取请求头中的用户信息
        String authHeader = request.getHeader(Constants.SystemInfo.AUTHORIZATION);
        if (StringUtils.isBlank(authHeader)) {
            SecurityRenderUtil.renderTokenErrorResponse(response);
            return;
        }
        String token = authHeader.substring(Constants.SystemInfo.AUTHORIZATION_PREFIX.length());
        String key = Constants.LoginUser.LOGIN_USER_PREFIX + jwtService.extractUsername(token);
        if (redisUtils.hasKey(key).orElse(false)) {
            // 删除token缓存
            redisUtils.delete(key);
            SecurityContextHolder.clearContext();
            SecurityRenderUtil.renderSuccessResponse(response, "登出成功", HttpStatus.OK);
        } else {
            SecurityRenderUtil.renderTokenErrorResponse(response);
        }
    }
}
