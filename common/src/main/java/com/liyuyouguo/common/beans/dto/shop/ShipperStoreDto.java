package com.liyuyouguo.common.beans.dto.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class ShipperStoreDto {

    private Integer id;

    private String name;

    private String code;

    @JsonProperty("sort_order")
    private Integer sortOrder;

    @JsonProperty("month_code")
    private String monthCode;

    @JsonProperty("customer_name")
    private String customerName;

    private Integer enabled;

}
