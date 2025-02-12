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
@Schema(description = "hiolabs_keywords 表")
@TableName("hiolabs_keywords")
public class Keywords {

    private Integer id;

    private String keyword;

    @JsonProperty("is_hot")
    private Boolean isHot;

    @JsonProperty("is_default")
    private Boolean isDefault;

    @JsonProperty("is_show")
    private Boolean isShow;

    @JsonProperty("sort_order")
    private Integer sortOrder;

    @JsonProperty("scheme_url")
    private String schemeUrl;

    private Integer type;

}