package com.liyuyouguo.common.entity.shop;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "hiolabs_order_express 表")
@TableName("hiolabs_order_express")
public class OrderExpress {

    private Integer id;

    @JsonProperty("order_id")
    private Integer orderId;

    @JsonProperty("shipper_id")
    private Integer shipperId;

    @JsonProperty("shipper_name")
    private String shipperName;

    @JsonProperty("shipper_code")
    private String shipperCode;

    @JsonProperty("logistic_code")
    private String logisticCode;

    private String traces;

    @JsonProperty("is_finish")
    private Integer isFinish;

    @JsonProperty("request_count")
    private Integer requestCount;

    @JsonProperty("request_time")
    private LocalDateTime requestTime;

    @JsonProperty("add_time")
    private LocalDateTime addTime;

    @JsonProperty("update_time")
    private LocalDateTime updateTime;

    @JsonProperty("express_type")
    private Integer expressType;

    @JsonProperty("express_status")
    private String expressStatus;

    @JsonProperty("region_code")
    private String regionCode;

}