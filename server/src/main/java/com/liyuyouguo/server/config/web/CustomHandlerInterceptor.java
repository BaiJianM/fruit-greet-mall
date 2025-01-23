package com.liyuyouguo.server.config.web;

import com.liyuyouguo.server.beans.UserInfo;
import com.liyuyouguo.server.config.web.security.jwt.JwtService;
import com.liyuyouguo.server.utils.RedisUtils;
import com.liyuyouguo.server.utils.UserInfoUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 自定义请求处理拦截器
 *
 * @author baijianmin
 */
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("all")
public class CustomHandlerInterceptor implements HandlerInterceptor {

    private final RedisUtils redisUtils;

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            // 获取请求头中的用户信息
            SecurityContext context = SecurityContextHolder.getContext();
            // UserInfoUtils中设置当前用户登录信息
            UserInfoUtils.setUserInfo((UserInfo) context.getAuthentication().getPrincipal());
        } catch (Exception e) {
            log.error("设置本地线程缓存的用户信息异常: ", e);
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        // UserInfoUtils中设置清除用户登录信息
        UserInfoUtils.clearUserInfo();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
