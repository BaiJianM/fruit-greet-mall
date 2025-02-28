package com.liyuyouguo.common.beans.vo;

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

    private String addTime;

    private String orderStatusText;

    private OrderHandleOptionVo handleOption;

}
