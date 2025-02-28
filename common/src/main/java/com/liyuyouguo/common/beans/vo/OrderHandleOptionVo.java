package com.liyuyouguo.common.beans.vo;

import lombok.Data;

/**
 * 订单可操作的选项
 *
 * @author baijianmin
 */
@Data
public class OrderHandleOptionVo {

    /**
     * 取消操作
     */
    private Boolean cancel;

    /**
     * 删除操作
     */
    private Boolean delete;

    /**
     * 支付操作
     */
    private Boolean pay;

    /**
     * 确认收货完成订单操作
     */
    private Boolean confirm;

    /**
     * 取消退款操作
     */
    private Boolean cancelRefund;

    public OrderHandleOptionVo() {
        this.cancel = false;
        this.delete = false;
        this.pay = false;
        this.confirm = false;
        this.cancelRefund = false;
    }
}
