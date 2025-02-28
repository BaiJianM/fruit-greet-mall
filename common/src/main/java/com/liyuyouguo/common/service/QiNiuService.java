package com.liyuyouguo.common.service;

import com.liyuyouguo.common.beans.vo.QiNiuTokenVo;
import com.liyuyouguo.common.config.qiniu.QiNiuProperties;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.DownloadUrl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.qiniu.util.Auth;

/**
 * 七牛云存储服务
 *
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QiNiuService {

    private final QiNiuProperties properties;

    public QiNiuTokenVo getToken() {
        Auth auth = Auth.create(properties.getAccessKey(), properties.getSecretKey());
        String upToken = auth.uploadToken(properties.getBucket());
        QiNiuTokenVo tokenVo = new QiNiuTokenVo();
        tokenVo.setToken(upToken);
        tokenVo.setUrl(properties.getDomain());
        return tokenVo;
    }

    public String getDownloadUrl(String key) {
        // useHttps 是否使用 https【必须】
        // key      下载资源在七牛云存储的 key【必须】
        DownloadUrl url = new DownloadUrl(properties.getDomain(), false, key);
//        url.setAttname(attname) // 配置 attname
//                .setFop(fop) // 配置 fop
//                .setStyle(style, styleSeparator, styleParam) // 配置 style

        // 带有效期
        long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
        long deadline = System.currentTimeMillis()/1000 + expireInSeconds;
        Auth auth = Auth.create(properties.getAccessKey(), properties.getSecretKey());
        String urlString = null;
        try {
            urlString = url.buildURL(auth, deadline);
        } catch (QiniuException e) {
            log.error("七牛云生成下载链接失败: {}", e.getMessage());
        }
        return urlString;
    }

}
