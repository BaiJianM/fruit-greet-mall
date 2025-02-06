package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author baijianmin
 */
@Data
public class AdminVo {

    private Integer id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("password_salt")
    private String passwordSalt;

    @JsonProperty("last_login_ip")
    private String lastLoginIp;

    @JsonProperty("last_login_time")
    private String lastLoginTime;

    @JsonProperty("is_delete")
    private Integer isDelete;

}
