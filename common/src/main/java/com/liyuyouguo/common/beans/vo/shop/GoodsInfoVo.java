package com.liyuyouguo.common.beans.vo.shop;

import com.liyuyouguo.common.entity.shop.Goods;
import com.liyuyouguo.common.entity.shop.GoodsGallery;
import com.liyuyouguo.common.entity.shop.Product;
import lombok.Data;

import java.util.List;

/**
 * 商品信息
 *
 * @author baijianmin
 */
@Data
public class GoodsInfoVo {

    private Goods info;

    private List<GoodsGallery> gallery;

    private ProductInfoVo specificationList;

    private List<Product> productList;

}
