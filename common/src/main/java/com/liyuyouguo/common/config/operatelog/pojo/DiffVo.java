package com.liyuyouguo.common.config.operatelog.pojo;

import lombok.Data;

import java.util.List;

/**
 * 发生变化的数据信息
 *
 * @author baijianmin
 */
@Data
public class DiffVo {

    /**
     * 实体类名
     */
    private String oldClassName;

    /**
     * 实体类别名
     */
    private String oldClassAlias;

    /**
     * 实体类名
     */
    private String newClassName;

    /**
     * 实体类别名
     */
    private String newClassAlias;

    /**
     * 发生变化的字段信息列表
     */
    private List<DiffFieldVo> diffFieldVoList;
}
