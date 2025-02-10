package com.liyuyouguo.common.beans.dto.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class ProductUpdateDto {

    @JsonProperty("goods_number")
    private Integer goodsNumber;

    @JsonProperty("goods_weight")
    private BigDecimal goodsWeight;

    @JsonProperty("goods_sn")
    private String goodsSn;

    @JsonProperty("retail_price")
    private BigDecimal retailPrice;

    private BigDecimal cost;

    private String value;

}
