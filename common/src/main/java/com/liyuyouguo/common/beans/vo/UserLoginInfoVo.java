package com.liyuyouguo.common.beans.vo;

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

    private Boolean isNew;

}
