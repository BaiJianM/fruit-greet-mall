package com.liyuyouguo.common.beans.dto.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class SaveGoodsListDto {

    private Integer id;

    @JsonProperty("order_id")
    private Integer orderId;

    private Integer number;

    @JsonProperty("retail_price")
    private BigDecimal retailPrice;

    private Integer addOrMinus;

}
