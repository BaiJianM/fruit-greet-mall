package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class OrderGoodsVo {

    @JsonProperty("goods_id")
    private Integer goodsId;

    @JsonProperty("list_pic_url")
    private String listPicUrl;

    @JsonProperty("goods_name")
    private String goodsName;

    @JsonProperty("goods_specifition_name_value")
    private String goodsSpecifitionNameValue;

    @JsonProperty("retail_price")
    private BigDecimal retailPrice;

    private Integer number;

}
