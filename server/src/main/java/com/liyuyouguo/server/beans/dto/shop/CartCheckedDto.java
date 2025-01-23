package com.liyuyouguo.server.beans.dto.shop;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 是否已选商品校验传参
 *
 * @author baijianmin
 */
@Data
public class CartCheckedDto {

    @NotBlank(message = "商品id不能为空")
    private String productIds;

    private Boolean isChecked;

}
