package com.liyuyouguo.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录/操作日志表
 *
 * @author baijianmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysOperateLog extends BaseEntity implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = -8872869422177124495L;

    /**
     * 日志类型（1，操作日志；2，登录日志）
     */
    private String type;

    /**
     * 功能模块（字典表枚举值）
     */
    private String module;

    /**
     * 操作事件
     */
    private String event;

    /**
     * 日志描述
     */
    private String msg;

    /**
     * 额外信息
     */
    private String extra;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 是否成功 默认1成功
     */
    private Integer isSuccess;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;
}