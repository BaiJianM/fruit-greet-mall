package com.liyuyouguo.common.beans.dto.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class CategorySaveDto {

    private Integer id;

    private String name;

    @JsonProperty("parent_id")
    private Integer parentId;

    @JsonProperty("sort_order")
    private Integer sortOrder;

    @JsonProperty("is_show")
    private Boolean isShow;

    @JsonProperty("is_channel")
    private Boolean isChannel;

    @JsonProperty("is_category")
    private Boolean isCategory;

}
