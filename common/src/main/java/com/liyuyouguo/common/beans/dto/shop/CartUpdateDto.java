package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

/**
 * 更新指定的购物车信息传参
 *
 * @author baijianmin
 */
@Data
public class CartUpdateDto {

    private Integer productId;

    private Integer cartId;

    private Integer number;

}
