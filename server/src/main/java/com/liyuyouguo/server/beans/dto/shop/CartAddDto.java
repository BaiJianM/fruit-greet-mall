package com.liyuyouguo.server.beans.dto.shop;

import lombok.Data;

/**
 * 添加商品到购物车传参
 *
 * @author baijianmin
 */
@Data
public class CartAddDto {

    private Integer goodsId;

    private Integer productId;

    private Integer number;

    private Integer addType;

}
