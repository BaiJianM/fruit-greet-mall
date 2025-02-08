package com.liyuyouguo.common.beans.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class OrderGoodsAdminVo {

    private Integer id;

    private Integer productId;

    private String goodsName;

    private String goodsAka;

    private String listPicUrl;

    private int number;

    private String goodsSpecifitionNameValue;

    private BigDecimal retailPrice;

    private String goodsSn;

}
