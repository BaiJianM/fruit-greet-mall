package com.liyuyouguo.common.entity.shop;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "hiolabs_order_goods 表")
@TableName("hiolabs_order_goods")
public class OrderGoods {

    private Integer id;

    @JsonProperty("order_id")
    private Integer orderId;

    @JsonProperty("goods_id")
    private Integer goodsId;

    @JsonProperty("goods_name")
    private String goodsName;

    @JsonProperty("goods_aka")
    private String goodsAka;

    @JsonProperty("product_id")
    private Integer productId;

    private Integer number;

    @JsonProperty("retail_price")
    private BigDecimal retailPrice;

    @JsonProperty("goods_specifition_name_value")
    private String goodsSpecifitionNameValue;

    @JsonProperty("goods_specifition_ids")
    private String goodsSpecifitionIds;

    @JsonProperty("list_pic_url")
    private String listPicUrl;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("is_delete")
    private Boolean isDelete;

}