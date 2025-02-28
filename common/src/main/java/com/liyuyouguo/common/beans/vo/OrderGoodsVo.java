package com.liyuyouguo.common.beans.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class OrderGoodsVo {

    private Integer goodsId;

    private String listPicUrl;

    private String goodsName;

    private String goodsSpecifitionNameValue;

    private BigDecimal retailPrice;

    private Integer number;

}
