package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class FreightTemplateGroupVo {

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
    private Boolean freeByNumber;

    @JsonProperty("free_by_money")
    private Boolean freeByMoney;

    @JsonProperty("is_delete")
    private Integer isDelete;

    private String areaName;

}
