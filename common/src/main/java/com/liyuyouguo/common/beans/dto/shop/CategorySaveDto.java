package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class CategorySaveDto {

    private Integer id;

    private String name;

    private Integer parentId;

    private Integer sortOrder;

    private Boolean isShow;

    private Boolean isChannel;

    private Boolean isCategory;

}
