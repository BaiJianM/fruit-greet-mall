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
@Schema(description = "hiolabs_freight_template 表")
@TableName("hiolabs_freight_template")
public class FreightTemplate {

    private Integer id;

    private String name;

    @JsonProperty("package_price")
    private BigDecimal packagePrice;

    @JsonProperty("freight_type")
    private Integer freightType;

    @JsonProperty("is_delete")
    private Boolean isDelete;

}