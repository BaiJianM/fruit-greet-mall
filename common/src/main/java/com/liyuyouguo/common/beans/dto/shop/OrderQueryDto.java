package com.liyuyouguo.common.beans.dto.shop;

import com.liyuyouguo.common.beans.FruitGreetPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author baijianmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderQueryDto extends FruitGreetPage {

    private Integer showType;

}
