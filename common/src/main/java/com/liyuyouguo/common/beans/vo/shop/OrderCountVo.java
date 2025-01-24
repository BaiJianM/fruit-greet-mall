package com.liyuyouguo.common.beans.vo.shop;

import lombok.Data;

/**
 * 订单数状态
 *
 * @author baijianmin
 */
@Data
public class OrderCountVo {

    private Long toPay;

    private Long toDelivery;

    private Long toReceive;

}
