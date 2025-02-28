package com.liyuyouguo.common.beans.dto.shop;

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

    private LocalDateTime registerTime;

    private LocalDateTime lastLoginTime;

    private String lastLoginIp;

    private String mobile;

    private String registerIp;

    private String avatar;

    private String weixinOpenid;

    private Integer nameMobile;

    private String country;

    private String province;

    private String city;

}
