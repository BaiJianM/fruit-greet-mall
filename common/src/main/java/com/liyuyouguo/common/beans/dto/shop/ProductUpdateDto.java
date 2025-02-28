package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class ProductUpdateDto {

    private Integer goodsNumber;

    private BigDecimal goodsWeight;

    private String goodsSn;

    private BigDecimal retailPrice;

    private BigDecimal cost;

    private String value;

}
