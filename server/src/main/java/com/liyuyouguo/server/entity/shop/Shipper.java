package com.liyuyouguo.server.entity.shop;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "hiolabs_shipper 表")
@TableName("hiolabs_shipper")
public class Shipper {

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