package com.liyuyouguo.common.beans.vo;

import lombok.Data;

import java.util.List;

/**
 * @author baijianmin
 */
@Data
public class CategoryOptionVo {

    private Integer value;

    private String label;

    private List<CategoryOptionVo> children;

}
