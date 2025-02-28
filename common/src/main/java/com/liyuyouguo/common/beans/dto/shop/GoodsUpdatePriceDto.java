package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class GoodsUpdatePriceDto {

    private String goodsName;

    private String goodsWeight;

    private Integer goodsId;

    private Integer id;

    private Integer goodsSpecificationIds;

    private BigDecimal retailPrice;

    private String goodsSn;

    private String value;

}
