package com.liyuyouguo.common.beans.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author baijianmin
 */
@Data
@AllArgsConstructor
public class RegionAdminVo {

    private Integer value;

    private String label;

    private List<RegionAdminVo> children;

}
