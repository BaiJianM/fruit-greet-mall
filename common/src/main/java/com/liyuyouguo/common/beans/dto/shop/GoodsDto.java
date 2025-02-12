package com.liyuyouguo.common.beans.dto.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liyuyouguo.common.beans.vo.GalleryDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author baijianmin
 */
@Data
public class GoodsDto {

    private Integer id;

    @JsonProperty("category_id")
    private Integer categoryId;

    @JsonProperty("is_on_sale")
    private Boolean isOnSale;

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
    private Boolean isIndex;

    @JsonProperty("is_new")
    private Boolean isNew;

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
    private Boolean isDelete;

    @JsonProperty("has_gallery")
    private Boolean hasGallery;

    @JsonProperty("has_done")
    private Boolean hasDone;

    @JsonProperty("category_name")
    private String categoryName;

    private List<GalleryDto> gallery;

}
