package com.liyuyouguo.common.beans.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class ProductVo {

    private Integer id;

    private Integer goodsId;

    private String goodsSpecificationIds;

    private String goodsSn;

    private Integer goodsNumber;

    private BigDecimal retailPrice;

    private BigDecimal cost;

    private Double goodsWeight;

    private Integer hasChange;

    private String goodsName;

    private String isOnSale;

    private Integer isDelete;

    private String value;

}
