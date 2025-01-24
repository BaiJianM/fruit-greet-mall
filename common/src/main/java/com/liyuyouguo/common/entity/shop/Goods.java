package com.liyuyouguo.common.entity.shop;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "hiolabs_goods 表")
@TableName("hiolabs_goods")
public class Goods {

    private Integer id;

    @JsonProperty("category_id")
    private Integer categoryId;

    @JsonProperty("is_on_sale")
    private Integer isOnSale;

    private String name;

    @JsonProperty("goods_number")
    private Integer goodsNumber;

    @JsonProperty("sell_volume")
    private Integer sellVolume;

    private String keywords;

    @JsonProperty("retail_price")
    private String retailPrice;

    @JsonProperty("min_retail_price")
    private BigDecimal minRetailPrice;

    @JsonProperty("cost_price")
    private String costPrice;

    @JsonProperty("min_cost_price")
    private BigDecimal minCostPrice;

    @JsonProperty("goods_brief")
    private String goodsBrief;

    @JsonProperty("goods_desc")
    private String goodsDesc;

    @JsonProperty("sort_order")
    private Integer sortOrder;

    @JsonProperty("is_index")
    private Integer isIndex;

    @JsonProperty("is_new")
    private Integer isNew;

    @JsonProperty("goods_unit")
    private String goodsUnit;

    @JsonProperty("https_pic_url")
    private String httpsPicUrl;

    @JsonProperty("list_pic_url")
    private String listPicUrl;

    @JsonProperty("freight_template_id")
    private Integer freightTemplateId;

    @JsonProperty("freight_type")
    private Integer freightType;

    @JsonProperty("is_delete")
    private Integer isDelete;

    @JsonProperty("has_gallery")
    private Integer hasGallery;

    @JsonProperty("has_done")
    private Integer hasDone;

}