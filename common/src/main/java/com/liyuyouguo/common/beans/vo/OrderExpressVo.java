package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author baijianmin
 */
@Data
public class OrderExpressVo {

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
    private Boolean isFinish;

    @JsonProperty("request_count")
    private Integer requestCount;

    @JsonProperty("request_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestTime;

    @JsonProperty("add_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addTime;

    @JsonProperty("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @JsonProperty("express_type")
    private Integer expressType;

    @JsonProperty("express_status")
    private String expressStatus;

    @JsonProperty("region_code")
    private String regionCode;

    @JsonProperty("MonthCode")
    private String monthCode;

    @JsonProperty("send_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

}
