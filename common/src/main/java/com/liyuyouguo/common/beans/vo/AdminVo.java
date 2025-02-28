package com.liyuyouguo.common.beans.vo;

import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class AdminVo {

    private Integer id;

    private String username;

    private String password;

    private String passwordSalt;

    private String lastLoginIp;

    private String lastLoginTime;

    private Integer isDelete;

}
