package com.liyuyouguo.common.beans.dto.shop;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 删除选中的购物车商品传参
 *
 * @author baijianmin
 */
@Data
public class CartDeleteDto {

    @NotEmpty(message = "商品id不能为空")
    private List<Integer> productIds;

}
