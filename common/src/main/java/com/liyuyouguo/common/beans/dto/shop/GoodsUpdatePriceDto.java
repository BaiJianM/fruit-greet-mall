package com.liyuyouguo.common.beans.dto.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class GoodsUpdatePriceDto {

    @JsonProperty("goods_name")
    private String goodsName;

    @JsonProperty("goods_weight")
    private String goodsWeight;

    @JsonProperty("goods_id")
    private Integer goodsId;

    private Integer id;

    private Integer goodsSpecificationIds;

    @JsonProperty("retail_price")
    private BigDecimal retailPrice;

    private String goodsSn;

    private String value;

}
