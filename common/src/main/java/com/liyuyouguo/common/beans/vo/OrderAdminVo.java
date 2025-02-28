package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private String offlinePay;

    private String orderSn;

    private String consignee;

    private String orderStatusText;

    private String fullRegion;

    private String postscript;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addTime;

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

    private String buttonText;

    private String mobile;

    private String address;

    private String userName;

    private String avatar;

    private String allAddress;

    private BigDecimal goodsPrice;

    private BigDecimal changePrice;

    private BigDecimal actualPrice;

    private BigDecimal freightPrice;

    private String adminMemo;

    private Boolean printStatus;

    private Integer orderStatus;

}
