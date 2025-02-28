package com.liyuyouguo.common.beans.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class CartTotalVo {

    private Integer goodsCount;

    private BigDecimal goodsAmount;

    private Integer checkedGoodsCount;

    private BigDecimal checkedGoodsAmount;

    private Integer userId;

    private Integer numberChange;

}
