package com.liyuyouguo.server.utils;

import com.liyuyouguo.server.commons.Constants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * IP地址工具
 *
 * @author baijianmin
 */
@Slf4j
public class IpUtils {

    private IpUtils() {
    }

    private static final String UNKNOWN = "unknown";

    /**
     * 获取IP地址
     *
     * @param request 请求对象
     * @return String ip地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("IpUtils ERROR ", e);
        }
        // 使用代理，则获取第一个IP地址
        if (!StringUtils.isEmpty(ip) && ip.length() > 15 && (ip.contains(Constants.FileInfo.COMMA))) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }
}
