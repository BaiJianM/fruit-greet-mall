package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 提交订单传参
 *
 * @author baijianmin
 */
@Data
public class OrderSubmitDto {

    private Integer addressId;

    private BigDecimal freightPrice;

    private Integer offlinePay;

    private String postscript;

}
