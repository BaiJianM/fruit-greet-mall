package com.liyuyouguo.server.config.operatelog.pojo;


import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 日志信息
 *
 * @author baijianmin
 */
@Data
public class OperateLogDto {
    /**
     * 操作事件
     */
    private String event;
    /**
     * 方法异常信息
     */
    private String exception;
    /**
     * 日志操作时间
     */
    private Date operateDate;
    /**
     * 方法是否成功
     */
    private Boolean success;
    /**
     * 日志内容
     */
    private String msg;
    /**
     * 日志标签
     */
    private String tag;
    /**
     * 方法结果
     */
    private String returnStr;
    /**
     * 方法执行时间（单位：毫秒）
     */
    private Long executionTime;
    /**
     * 额外信息
     */
    private String extra;
    /**
     * 操作人ID
     */
    private Long operatorId;
    /**
     * 操作人姓名
     */
    private String operatorName;
    /**
     * ip地址
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
     * 实体DIFF列表
     */
    private List<DiffVo> diffVoList;

}