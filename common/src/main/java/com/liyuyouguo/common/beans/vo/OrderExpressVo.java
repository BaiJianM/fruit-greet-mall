package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author baijianmin
 */
@Data
public class OrderExpressVo {

    private Integer id;

    private Integer orderId;

    private Integer shipperId;

    private String shipperName;

    private String shipperCode;

    private String logisticCode;

    private String traces;

    private Boolean isFinish;

    private Integer requestCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Integer expressType;

    private String expressStatus;

    private String regionCode;

    private String monthCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

}
