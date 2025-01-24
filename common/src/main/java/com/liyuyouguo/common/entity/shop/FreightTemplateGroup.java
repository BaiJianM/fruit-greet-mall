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
@Schema(description = "hiolabs_freight_template_group 表")
@TableName("hiolabs_freight_template_group")
public class FreightTemplateGroup {

    private Integer id;

    @JsonProperty("template_id")
    private Integer templateId;

    @JsonProperty("is_default")
    private Integer isDefault;

    private String area;

    private Integer start;

    @JsonProperty("start_fee")
    private BigDecimal startFee;

    private Integer add;

    @JsonProperty("add_fee")
    private BigDecimal addFee;

    @JsonProperty("free_by_number")
    private Integer freeByNumber;

    @JsonProperty("free_by_money")
    private BigDecimal freeByMoney;

    @JsonProperty("is_delete")
    private Integer isDelete;

}