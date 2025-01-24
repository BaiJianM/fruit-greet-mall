package com.liyuyouguo.common.beans.vo.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("user_id")
    private Integer userId;

    private Integer numberChange;

}
