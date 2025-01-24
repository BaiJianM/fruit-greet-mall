package com.liyuyouguo.common.config.operatelog.pojo;

import lombok.Data;

/**
 * 发生变化的字段信息
 *
 * @author baijianmin
 */
@Data
public class DiffFieldVo {

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 字段别名
     */
    private String fieldAlias;

    /**
     * 旧值
     */
    private Object oldValue;

    /**
     * 新值
     */
    private Object newValue;
}
