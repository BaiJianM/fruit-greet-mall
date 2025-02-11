package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class WapIndexVo {

    @JsonProperty("goods_sn")
    private String goodsSn;

    @JsonProperty("goods_id")
    private Integer goodsId;

    @JsonProperty("goods_specification_ids")
    private String goodsSpecificationIds;

    @JsonProperty("retail_price")
    private BigDecimal retailPrice;

    private String value;

    private String name;

    @JsonProperty("is_on_sale")
    private Boolean isOnSale;

    @JsonProperty("list_pic_url")
    private String listPicUrl;

}
