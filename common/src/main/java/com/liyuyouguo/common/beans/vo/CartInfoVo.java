package com.liyuyouguo.common.beans.vo;

import lombok.Data;

import java.util.List;

/**
 * 购物车信息
 *
 * @author baijianmin
 */
@Data
public class CartInfoVo {

    private List<CartVo> cartList;

    private CartTotalVo cartTotal;

}
