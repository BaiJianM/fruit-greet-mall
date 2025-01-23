package com.liyuyouguo.server.beans.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息
 *
 * @author baijianmin
 */
@Data
@Schema(description = "系统用户信息VO")
public class UserInfoVo {

    @Schema(description = "用户id")
    protected Long userId;

    @Schema(description = "用户真实姓名")
    protected String realName;

    @Schema(description = "用户头像地址")
    protected String avatar;

    @Schema(description = "手机号")
    protected String phone;

    @Schema(description = "邮箱")
    protected String email;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime createTime;

    @Schema(description = "就职单位")
    protected String employedBy;

    @Schema(description = "角色id")
    protected Long roleId;

    @Schema(description = "角色名称")
    protected String roleName;

    @Schema(description = "角色编号")
    protected String roleCode;

    @Schema(description = "权限编号列表")
    protected List<String> permCodeList;

//    @Schema(description = "路由信息列表")
//    protected List<RouterTreeResultVo> routerList;

    @Schema(description = "账号是否已被删除")
    protected Boolean isDelete;

    @Schema(description = "账号是否启用")
    protected Boolean isEnable;

    @Schema(description = "是否web管理员")
    private Boolean isAdmin;
}
