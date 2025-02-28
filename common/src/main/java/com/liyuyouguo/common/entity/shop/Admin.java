package com.liyuyouguo.common.entity.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@Schema(description = "admin表")
public class Admin {

    /**
     * 主键
     */
    @Schema(description = "主键", example = "1")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @Schema(description = "账号名", example = "1")
    private String username;

    @Schema(description = "密码", example = "1")
    private String password;

    @Schema(description = "最后登录的IP", example = "1")
    private String lastLoginIp;

    @Schema(description = "最后登录时间", example = "1")
    private LocalDateTime lastLoginTime;

    @Schema(description = "unknown", example = "1")
    private Boolean isDelete;

}