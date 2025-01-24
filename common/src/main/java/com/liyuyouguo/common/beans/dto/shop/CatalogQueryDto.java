package com.liyuyouguo.common.beans.dto.shop;

import com.liyuyouguo.common.beans.FruitShopPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分类目录显示传参
 *
 * @author baijianmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CatalogQueryDto extends FruitShopPage {

    /**
     * 分类id
     */
    private Integer id;

}
