package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liyuyouguo.common.entity.shop.OrderGoods;
import lombok.Data;

import java.util.List;

/**
 * @author baijianmin
 */
@Data
public class OrderVo {

    private Integer id;

    private List<OrderGoods> goodsList;

    private Integer goodsCount;

    @JsonProperty("add_time")
    private String addTime;

    @JsonProperty("order_status_text")
    private String orderStatusText;

    private OrderHandleOptionVo handleOption;

}
