package com.liyuyouguo.common.entity.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "user 表")
public class User {

    /**
     * 主键
     */
    @Schema(description = "主键", example = "1")
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String nickname;

    private String name;

    private String username;

    private String password;

    private Integer gender;

    private Integer birthday;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registerTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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