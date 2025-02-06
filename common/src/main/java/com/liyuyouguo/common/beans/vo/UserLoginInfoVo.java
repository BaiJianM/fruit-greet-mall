package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liyuyouguo.common.entity.shop.User;
import lombok.Data;

/**
 * 小程序用户登录或注册返回
 *
 * @author baijianmin
 */
@Data
public class UserLoginInfoVo {

    private String token;

    private User userInfo;

    @JsonProperty("is_new")
    private Boolean isNew;

}
