package com.liyuyouguo.common.beans.vo;

import com.liyuyouguo.common.entity.shop.OrderGoods;
import lombok.Data;

import java.util.List;

/**
 * 订单详情
 *
 * @author baijianmin
 */
@Data
public class OrderDetailVo {

    private OrderInfoVo orderInfo;

    private List<OrderGoods> orderGoods;

    private OrderHandleOptionVo handleOption;

    private OrderTextCodeVo textCode;

    private Integer goodsCount;

}
