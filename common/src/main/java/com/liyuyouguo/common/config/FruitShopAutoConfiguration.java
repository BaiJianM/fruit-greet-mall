package com.liyuyouguo.common.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.liyuyouguo.common.commons.Constants;
import com.liyuyouguo.common.config.mybatis.FillMetaObjectHandler;
import com.liyuyouguo.common.config.operatelog.properties.OperateLogProperties;
import com.liyuyouguo.common.config.web.CustomHandlerInterceptor;
import com.liyuyouguo.common.config.web.security.SecurityProperties;
import com.liyuyouguo.common.config.web.security.jwt.JwtService;
import com.liyuyouguo.common.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 项目自动配置
 *
 * @author baijianmin
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({FruitShopProperties.class})
@EnableTransactionManagement
public class FruitShopAutoConfiguration {
    // region mysql数据库配置

    /**
     * 乐观锁分页插件配置
     *
     * @return MybatisPlusInterceptor mybatis-plus拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 乐观锁
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 实体类字段自动填充处理器
     *
     * @return FillMetaHandler
     */
    @Bean
    public FillMetaObjectHandler fillMetaObjectHandler() {
        return new FillMetaObjectHandler();
    }

    // endregion

    // region redis配置

    /**
     * 配置模板客户端
     *
     * @param factory 使用lettuce客户端连接
     * @return redis模板客户端
     */
    @Bean
    public RedisTemplate<String, Object> initRedisTemplate(LettuceConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = createRedisSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key 采用 String 的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash 的 key 也采用 String 的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value 序列化方式采用 jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash 的 value 序列化方式采用 jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 以jackson作为redis的序列化器
     *
     * @return jackson序列化器
     */
    private static Jackson2JsonRedisSerializer<Object> createRedisSerializer() {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY);
        return new Jackson2JsonRedisSerializer<>(om, Object.class);
    }

    // endregion

    // region spring security配置

    /**
     * spring security密码加密策略
     *
     * @return PasswordEncoder 加密编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // endregion

    // region webmvc配置

    /**
     * 添加自定义请求拦截器
     *
     * @param redisUtils redis工具
     * @return WebMvcConfigurer webmvc配置
     */
    @Bean
    public WebMvcConfigurer customMvcConfigurer(RedisUtils redisUtils, JwtService jwtService,
                                                SecurityProperties securityProperties) {
        return new CustomWebMvcConfigurer(redisUtils, jwtService, securityProperties);
    }

    /**
     * webmvc配置
     *
     * @author baijianmin
     */
    private record CustomWebMvcConfigurer(RedisUtils redisUtils, JwtService jwtService,
                                          SecurityProperties securityProperties) implements WebMvcConfigurer {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new CustomHandlerInterceptor(redisUtils, jwtService))
                    .addPathPatterns("/**")
                    .excludePathPatterns("/", "/css/**", "fonts/**", "/images/**", "/js/**")
                    .excludePathPatterns(Constants.SystemInfo.LOGIN_URL)
                    .excludePathPatterns("/api" + Constants.SystemInfo.LOGIN_URL)
                    .excludePathPatterns(securityProperties.getIgnoreUrls());
        }
    }

    // endregion

    @Bean
    public WxMaService wxMaService(FruitShopProperties properties) {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(properties.getAppid());
        config.setSecret(properties.getSecret());
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        return service;
    }
}
