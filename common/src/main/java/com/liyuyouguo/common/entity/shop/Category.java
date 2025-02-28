package com.liyuyouguo.common.entity.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Category {

    /**
     * 主键
     */
    @Schema(description = "主键", example = "1")
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String keywords;

    private String frontDesc;

    private Integer parentId;

    private Integer sortOrder;

    private Integer showIndex;

    private Boolean isShow;

    private String iconUrl;

    private String imgUrl;

    private String level;

    private String frontName;

    @JsonProperty("pHeight")
    private Integer pHeight;

    private Boolean isCategory;

    private Boolean isChannel;

}