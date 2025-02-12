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
@Schema(description = "hiolabs_goods_gallery 表")
@TableName("hiolabs_goods_gallery")
public class GoodsGallery {

    private Integer id;

    @JsonProperty("goods_id")
    private Integer goodsId;

    @JsonProperty("img_url")
    private String imgUrl;

    @JsonProperty("img_desc")
    private String imgDesc;

    @JsonProperty("sort_order")
    private Integer sortOrder;

    @JsonProperty("is_delete")
    private Boolean isDelete;

}