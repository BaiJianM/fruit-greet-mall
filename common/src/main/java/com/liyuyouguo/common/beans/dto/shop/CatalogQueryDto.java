package com.liyuyouguo.common.beans.dto.shop;

import com.liyuyouguo.common.beans.FruitGreetPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分类目录显示传参
 *
 * @author baijianmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CatalogQueryDto extends FruitGreetPage {

    /**
     * 分类id
     */
    private Integer id;

}
