package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liyuyouguo.common.entity.shop.OrderGoods;
import com.liyuyouguo.common.entity.shop.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author baijianmin
 */
@Data
public class OrderAdminVo {

    private Long id;

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

}
