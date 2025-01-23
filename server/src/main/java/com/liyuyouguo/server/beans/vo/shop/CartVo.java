package com.liyuyouguo.server.beans.vo.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liyuyouguo.server.entity.shop.Cart;
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
