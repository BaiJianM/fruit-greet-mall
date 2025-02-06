package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liyuyouguo.common.entity.shop.Cart;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author baijianmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CartVo extends Cart {

    @JsonProperty("weight_count")
    private Double weightCount;

    @JsonProperty("goods_number")
    private Integer goodsNumber;

}
