package com.liyuyouguo.server.beans.dto.shop;

import com.liyuyouguo.server.beans.FruitShopPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author baijianmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderQueryDto extends FruitShopPage {

    private Integer showType;

}
