package com.liyuyouguo.common.service;

import com.google.code.kaptcha.Producer;
import com.liyuyouguo.common.beans.vo.authcode.AuthCodeResultVo;
import com.liyuyouguo.common.commons.Constants;
import com.liyuyouguo.common.commons.FruitShopException;
import com.liyuyouguo.common.commons.SystemError;
import com.liyuyouguo.common.config.web.authcode.AuthCodeTypeEnum;
import com.liyuyouguo.common.config.web.authcode.KaptchaProperties;
import com.liyuyouguo.common.utils.RedisUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务类
 *
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaService {

    /**
     * 注入字符类型的验证码生成器
     */
    @Resource(name = "captchaProducerText")
    private Producer captchaProducer;

    /**
     * 注入数字类型的验证码生成器
     */
    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    private final RedisUtils redisUtils;

    private final KaptchaProperties properties;

    /**
     * 获取图形验证码，返回图片base64字符
     *
     * @return AuthCodeResultVO 图形验证码信息
     */
    public AuthCodeResultVo getAuthCode() {
        AuthCodeResultVo result = new AuthCodeResultVo();
        boolean captchaEnabled = properties.getIsEnable();
        result.setCaptchaEnabled(captchaEnabled);
        if (!captchaEnabled) {
            return result;
        }
        // 保存验证码信息
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String verifyKey = Constants.AuthCode.CAPTCHA_CODE_KEY + uuid;

        String capStr, code = null;
        BufferedImage image = null;
        // 生成验证码
        AuthCodeTypeEnum captchaType = properties.getAuthCodeType();
        if (captchaType == AuthCodeTypeEnum.MATH) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if (captchaType == AuthCodeTypeEnum.CHAR) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }
        redisUtils.setEx(verifyKey, code, properties.getTimeout(), TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            assert image != null;
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            throw new FruitShopException(SystemError.AUTH_CODE_ERROR);
        }
        result.setUuid(uuid);
        result.setImg(new String(Base64.getEncoder().encode(os.toByteArray()), StandardCharsets.UTF_8));
        return result;
    }

    /**
     * 校验图片验证码
     *
     * @param authCode 验证码
     * @param uuid     验证码缓存key
     * @return boolean 验证码校验结果
     */
    public boolean verify(String authCode, String uuid) {
        boolean captchaEnabled = properties.getIsEnable();
        String verifyKey = Constants.AuthCode.CAPTCHA_CODE_KEY + uuid;
        Object code = redisUtils.get(verifyKey).orElse("");
        redisUtils.delete(verifyKey);
        return !captchaEnabled || code.equals(authCode);
    }

}
