package com.liyuyouguo.common.beans.dto.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class GoodsSpecificationDto {

    private Integer id;

    @JsonProperty("goods_sn")
    private String goodsSn;

    private String value;

    private BigDecimal cost;

    @JsonProperty("retail_price")
    private BigDecimal retailPrice;

    @JsonProperty("goods_weight")
    private Double goodsWeight;

    @JsonProperty("goods_number")
    private Integer goodsNumber;

}
