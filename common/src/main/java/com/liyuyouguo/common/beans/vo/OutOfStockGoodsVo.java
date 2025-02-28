package com.liyuyouguo.common.beans.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class OutOfStockGoodsVo {

    private Integer id;

    private Integer categoryId;

    private Boolean isOnSale;

    private String name;

    private Integer goodsNumber;

    private Integer sellVolume;

    private String keywords;

    private String retailPrice;

    private BigDecimal minRetailPrice;

    private String costPrice;

    private BigDecimal minCostPrice;

    private String goodsBrief;

    private String goodsDesc;

    private Integer sortOrder;

    private Integer isIndex;

    private Integer isNew;

    private String goodsUnit;

    private String httpsPicUrl;

    private String listPicUrl;

    private Integer freightTemplateId;

    private Integer freightType;

    private Integer isDelete;

    private Integer hasGallery;

    private Integer hasDone;

    private String categoryName;

    private String categoryPName;

}
