package com.liyuyouguo.common.entity.shop;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "cart表")
@TableName("hiolabs_cart")
public class Cart {

    private Integer id;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("goods_id")
    private Integer goodsId;

    @JsonProperty("goods_sn")
    private String goodsSn;

    @JsonProperty("product_id")
    private Integer productId;

    @JsonProperty("goods_name")
    private String goodsName;

    @JsonProperty("goods_aka")
    private String goodsAka;

    @JsonProperty("goods_weight")
    private Double goodsWeight;

    @JsonProperty("add_price")
    private BigDecimal addPrice;

    @JsonProperty("retail_price")
    private BigDecimal retailPrice;

    private Integer number;

    @JsonProperty("goods_specifition_name_value")
    private String goodsSpecifitionNameValue;

    @JsonProperty("goods_specifition_ids")
    private String goodsSpecifitionIds;

    private Boolean checked;

    @JsonProperty("list_pic_url")
    private String listPicUrl;

    @JsonProperty("freight_template_id")
    private Integer freightTemplateId;

    @JsonProperty("is_on_sale")
    private Boolean isOnSale;

    @JsonProperty("add_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addTime;

    @JsonProperty("is_fast")
    private Boolean isFast;

    @JsonProperty("is_delete")
    private Boolean isDelete;

}