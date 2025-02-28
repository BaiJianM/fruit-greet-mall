package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class GoodsSpecificationDto {

    private Integer id;

    private String goodsSn;

    private String value;

    private BigDecimal cost;

    private BigDecimal retailPrice;

    private Double goodsWeight;

    private Integer goodsNumber;

}
