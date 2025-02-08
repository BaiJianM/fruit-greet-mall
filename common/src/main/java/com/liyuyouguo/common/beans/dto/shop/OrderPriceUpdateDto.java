package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class OrderPriceUpdateDto {

    private Integer orderId;

    private BigDecimal goodsPrice;

    private BigDecimal freightPrice;

    private BigDecimal actualPrice;

}
