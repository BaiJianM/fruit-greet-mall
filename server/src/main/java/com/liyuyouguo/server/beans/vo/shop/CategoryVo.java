package com.liyuyouguo.server.beans.vo.shop;

import com.liyuyouguo.server.entity.shop.Category;
import com.liyuyouguo.server.entity.shop.Goods;
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
