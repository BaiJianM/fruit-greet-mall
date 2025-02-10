package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liyuyouguo.common.entity.shop.Cart;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author baijianmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ShopCartVo extends Cart {

    private String nickname;

}
