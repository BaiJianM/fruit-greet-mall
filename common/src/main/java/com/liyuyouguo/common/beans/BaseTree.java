package com.liyuyouguo.common.beans;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 树结构的基类
 *
 * @author baijianmin
 */
@Data
public class BaseTree implements Serializable {

    @Serial
    private static final long serialVersionUID = 8481964221820200858L;

    /**
     * 主键id（树形结构展示用）
     */
    private Long id;

    /**
     * 父级id（树形结构展示用）
     */
    private Long parentId;

    @Schema(description = "子集列表（树形结构展示用）")
    private List<? extends BaseTree> children;

    public boolean hasChildren() {
        return !children.isEmpty();
    }

}
