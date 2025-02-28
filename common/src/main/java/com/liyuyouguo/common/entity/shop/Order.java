package com.liyuyouguo.common.entity.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Schema(description = "order 表")
public class Order {

    /**
     * 主键
     */
    @Schema(description = "主键", example = "1")
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String orderSn;

    private Integer userId;

    private Integer orderStatus;

    private Integer offlinePay;

    private Integer shippingStatus;

    private Boolean printStatus;

    private Integer payStatus;

    private String consignee;

    private Integer country;

    private Integer province;

    private Integer city;

    private Integer district;

    private String address;

    private String printInfo;

    private String mobile;

    private String postscript;

    private String adminMemo;

    private Double shippingFee;

    private String payName;

    private String payId;

    private BigDecimal changePrice;

    private BigDecimal actualPrice;

    private BigDecimal orderPrice;

    private BigDecimal goodsPrice;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime shippingTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dealDoneTime;

    private BigDecimal freightPrice;

    private BigDecimal expressValue;

    private String remark;

    private Integer orderType;

    private Boolean isDelete;

}