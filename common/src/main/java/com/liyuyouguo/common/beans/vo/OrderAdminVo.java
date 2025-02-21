package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liyuyouguo.common.entity.shop.OrderGoods;
import com.liyuyouguo.common.entity.shop.User;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author baijianmin
 */
@Data
public class OrderAdminVo {

    private Integer id;

    @JsonProperty("offline_pay")
    private String offlinePay;

    @JsonProperty("order_sn")
    private String orderSn;

    private String consignee;

    @JsonProperty("order_status_text")
    private String orderStatusText;

    @JsonProperty("full_region")
    private String fullRegion;

    private String postscript;

    @JsonProperty("add_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addTime;

    @JsonProperty("pay_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime shippingTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dealdoneTime;

    private String expressInfo;

    private List<OrderGoods> goodsList;

    private int goodsCount;

    private User userInfo;

    @JsonProperty("button_text")
    private String buttonText;

    private String mobile;

    private String address;

    @JsonProperty("user_name")
    private String userName;

    private String avatar;

    private String allAddress;

    @JsonProperty("goods_price")
    private BigDecimal goodsPrice;

    @JsonProperty("change_price")
    private BigDecimal changePrice;

    @JsonProperty("actual_price")
    private BigDecimal actualPrice;

    @JsonProperty("freight_price")
    private BigDecimal freightPrice;

    @JsonProperty("admin_memo")
    private String adminMemo;

    @JsonProperty("print_status")
    private Boolean printStatus;

    @JsonProperty("order_status")
    private Integer orderStatus;

}
