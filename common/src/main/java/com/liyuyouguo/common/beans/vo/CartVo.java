package com.liyuyouguo.common.beans.vo;

import com.liyuyouguo.common.entity.shop.Cart;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author baijianmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CartVo extends Cart {

    private Double weightCount;

    private Integer goodsNumber;

}
