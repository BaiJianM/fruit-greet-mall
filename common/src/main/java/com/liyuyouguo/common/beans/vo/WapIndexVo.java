package com.liyuyouguo.common.beans.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class WapIndexVo {

    private String goodsSn;

    private Integer goodsId;

    private String goodsSpecificationIds;

    private BigDecimal retailPrice;

    private String value;

    private String name;

    private Boolean isOnSale;

    private String listPicUrl;

}
