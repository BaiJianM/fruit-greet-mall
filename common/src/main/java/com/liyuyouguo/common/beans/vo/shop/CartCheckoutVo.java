package com.liyuyouguo.common.beans.vo.shop;

import com.liyuyouguo.common.entity.shop.Address;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单提交前的检验和填写相关订单信息
 *
 * @author baijianmin
 */
@Data
public class CartCheckoutVo {

    private Address checkedAddress;

    private BigDecimal freightPrice;

    private List<CartVo> checkedGoodsList;

    private BigDecimal goodsTotalPrice;

    private BigDecimal orderTotalPrice;

    private BigDecimal actualPrice;

    private Integer goodsCount;

    private Integer outStock;

    private Integer numberChange;

}
