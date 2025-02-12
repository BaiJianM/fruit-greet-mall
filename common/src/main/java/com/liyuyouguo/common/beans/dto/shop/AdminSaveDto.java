package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class AdminSaveDto {

    private Integer id;

    private String username;

    private String newPassword;

    private String passwordSalt;

    private Boolean isShow;

    private Boolean isNew;
}
