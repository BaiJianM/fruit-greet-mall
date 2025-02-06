package com.liyuyouguo.common.beans.vo;

import com.liyuyouguo.common.entity.shop.Category;
import com.liyuyouguo.common.entity.shop.Goods;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author baijianmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryVo extends Category {

    private List<Goods> goodsList;
}
