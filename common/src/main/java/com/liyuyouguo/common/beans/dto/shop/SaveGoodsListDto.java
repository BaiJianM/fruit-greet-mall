package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class SaveGoodsListDto {

    private Integer id;

    private Integer orderId;

    private Integer number;

    private BigDecimal retailPrice;

    private Integer addOrMinus;

}
