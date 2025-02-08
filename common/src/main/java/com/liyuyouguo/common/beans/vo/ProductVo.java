package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class ProductVo {

    private Integer id;

    @JsonProperty("goods_id")
    private Integer goodsId;

    @JsonProperty("goods_specification_ids")
    private String goodsSpecificationIds;

    @JsonProperty("goods_sn")
    private String goodsSn;

    @JsonProperty("goods_number")
    private Integer goodsNumber;

    @JsonProperty("retail_price")
    private BigDecimal retailPrice;

    private BigDecimal cost;

    @JsonProperty("goods_weight")
    private Double goodsWeight;

    @JsonProperty("has_change")
    private Integer hasChange;

    @JsonProperty("goods_name")
    private String goodsName;

    @JsonProperty("is_on_sale")
    private String isOnSale;

    @JsonProperty("is_delete")
    private Integer isDelete;

    private String value;

}
