package com.liyuyouguo.common.beans.dto.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author baijianmin
 */
@Data
public class UserStoreDto {

    private Integer id;

    private String nickname;

    private String name;

    private String username;

    private String password;

    private Integer gender;

    private Integer birthday;

    @JsonProperty("register_time")
    private LocalDateTime registerTime;

    @JsonProperty("last_login_time")
    private LocalDateTime lastLoginTime;

    @JsonProperty("last_login_ip")
    private String lastLoginIp;

    private String mobile;

    @JsonProperty("register_ip")
    private String registerIp;

    private String avatar;

    @JsonProperty("weixin_openid")
    private String weixinOpenid;

    @JsonProperty("name_mobile")
    private Integer nameMobile;

    private String country;

    private String province;

    private String city;

}
