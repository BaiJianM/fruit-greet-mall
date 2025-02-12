package com.liyuyouguo.common.entity.shop;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "category表")
@TableName("hiolabs_category")
public class Category {

    private Integer id;

    private String name;

    private String keywords;

    @JsonProperty("front_desc")
    private String frontDesc;

    @JsonProperty("parent_id")
    private Integer parentId;

    @JsonProperty("sort_order")
    private Integer sortOrder;

    @JsonProperty("show_index")
    private Integer showIndex;

    @JsonProperty("is_show")
    private Boolean isShow;

    @JsonProperty("icon_url")
    private String iconUrl;

    @JsonProperty("img_url")
    private String imgUrl;

    private String level;

    @JsonProperty("front_name")
    private String frontName;

    @JsonProperty("p_height")
    private Integer pHeight;

    @JsonProperty("is_category")
    private Boolean isCategory;

    @JsonProperty("is_channel")
    private Boolean isChannel;

}