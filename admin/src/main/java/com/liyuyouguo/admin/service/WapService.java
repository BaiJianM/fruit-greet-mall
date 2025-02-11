package com.liyuyouguo.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.beans.dto.shop.UpdatePriceDto;
import com.liyuyouguo.common.beans.vo.*;
import com.liyuyouguo.common.entity.shop.Cart;
import com.liyuyouguo.common.entity.shop.Category;
import com.liyuyouguo.common.entity.shop.Goods;
import com.liyuyouguo.common.entity.shop.Product;
import com.liyuyouguo.common.mapper.*;
import com.liyuyouguo.common.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WapService {

    private final ProductMapper productMapper;

    private final GoodsSpecificationMapper goodsSpecificationMapper;

    private final GoodsMapper goodsMapper;

    private final CartMapper cartMapper;

    private final CategoryMapper categoryMapper;

    public List<WapIndexVo> getProductList() {
        List<WapIndexVo> productInfoList = productMapper.getProductInfoList();
        List<Goods> goods = goodsMapper.selectList(Wrappers.lambdaQuery(Goods.class).eq(Goods::getIsDelete, 0));
        for (WapIndexVo wapIndexVo : productInfoList) {
            Integer goodsId = wapIndexVo.getGoodsId();
            for (Goods good : goods) {
                if (goodsId.equals(good.getId())) {
                    wapIndexVo.setName(good.getName() + "-" + wapIndexVo.getValue());
                    wapIndexVo.setIsOnSale(good.getIsOnSale() == 1);
                    wapIndexVo.setListPicUrl(good.getListPicUrl());
                }
            }
        }
        return productInfoList;
    }

    public List<WapIndexVo> getOnSaleList() {
        List<WapIndexVo> productInfoList = productMapper.getProductInfoList();
        List<Goods> goods = goodsMapper.selectList(Wrappers.lambdaQuery(Goods.class)
                .eq(Goods::getIsDelete, 0)
                .eq(Goods::getIsOnSale, 1)
        );
        List<WapIndexVo> onSaleList = new ArrayList<>();
        for (WapIndexVo wapIndexVo : productInfoList) {
            Integer goodsId = wapIndexVo.getGoodsId();
            for (Goods good : goods) {
                if (goodsId.equals(good.getId())) {
                    wapIndexVo.setName(good.getName() + "-" + wapIndexVo.getValue());
                    wapIndexVo.setListPicUrl(good.getListPicUrl());
                    if (wapIndexVo.getIsOnSale()) {
                        onSaleList.add(wapIndexVo);
                    }
                }
            }
        }
        return onSaleList;
    }

    public List<WapIndexVo> getOutSaleList() {
        List<WapIndexVo> productInfoList = productMapper.getProductInfoList();
        List<Goods> goods = goodsMapper.selectList(Wrappers.lambdaQuery(Goods.class)
                .eq(Goods::getIsDelete, 0)
                .eq(Goods::getIsOnSale, 0)
        );
        List<WapIndexVo> outSaleList = new ArrayList<>();
        for (WapIndexVo wapIndexVo : productInfoList) {
            Integer goodsId = wapIndexVo.getGoodsId();
            for (Goods good : goods) {
                if (goodsId.equals(good.getId())) {
                    wapIndexVo.setName(good.getName() + "-" + wapIndexVo.getValue());
                    wapIndexVo.setListPicUrl(good.getListPicUrl());
                    if (!wapIndexVo.getIsOnSale()) {
                        outSaleList.add(wapIndexVo);
                    }
                }
            }
        }
        return outSaleList;
    }

    public void updatePrice(UpdatePriceDto dto) {
        // 更新商品价格
        productMapper.update(Wrappers.lambdaUpdate(Product.class)
                .eq(Product::getGoodsSn, dto.getSn())
                .set(Product::getRetailPrice, dto.getPrice()));

        // 获取最低价格
        BigDecimal minPrice = productMapper.selectList(
                Wrappers.lambdaQuery(Product.class).eq(Product::getGoodsId, dto.getId())
        ).stream().map(Product::getRetailPrice).min(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);

        // 更新购物车中的商品价格
        cartMapper.update(Wrappers.lambdaUpdate(Cart.class)
                .eq(Cart::getGoodsSn, dto.getSn())
                .set(Cart::getRetailPrice, dto.getPrice()));

        // 更新goods表中的商品价格
        goodsMapper.update(Wrappers.lambdaUpdate(Goods.class)
                .eq(Goods::getId, dto.getId())
                .set(Goods::getRetailPrice, minPrice));
    }

    public PageResult<OutOfStockGoodsVo> getOutOfStockGoods(Integer page, Integer size) {
        Page<Goods> goodsPage = goodsMapper.selectPage(
                new Page<>(page, size),
                Wrappers.lambdaQuery(Goods.class)
                        .eq(Goods::getIsDelete, 0)
                        .le(Goods::getGoodsNumber, 0)
                        .orderByDesc(Goods::getId)
        );

        return ConvertUtils.convert(goodsPage, PageResult<OutOfStockGoodsVo>::new, (s, t) -> t.setRecords(this.getGoods(goodsPage))).orElseThrow();
    }

    public PageResult<OutOfStockGoodsVo> getDropGoods(Integer page, Integer size) {
        Page<Goods> goodsPage = goodsMapper.selectPage(
                new Page<>(page, size),
                Wrappers.lambdaQuery(Goods.class)
                        .eq(Goods::getIsDelete, 0)
                        .eq(Goods::getIsOnSale, 0)
                        .orderByDesc(Goods::getId)
        );

        return ConvertUtils.convert(goodsPage, PageResult<OutOfStockGoodsVo>::new, (s, t) -> t.setRecords(this.getGoods(goodsPage))).orElseThrow();
    }

    public PageResult<OutOfStockGoodsVo> sort(Integer page, Integer size, Integer index) {
        LambdaQueryWrapper<Goods> wrapper = Wrappers.lambdaQuery(Goods.class).eq(Goods::getIsDelete, 0);
        if (index == 1) {
            wrapper.orderByDesc(Goods::getSellVolume);
        } else if (index == 2) {
            wrapper.orderByDesc(Goods::getRetailPrice);
        } else if (index == 3) {
            wrapper.orderByDesc(Goods::getGoodsNumber);
        }
        Page<Goods> goodsPage = goodsMapper.selectPage(new Page<>(page, size), wrapper);
        return ConvertUtils.convert(goodsPage, PageResult<OutOfStockGoodsVo>::new, (s, t) -> t.setRecords(this.getGoods(goodsPage))).orElseThrow();
    }

    private List<OutOfStockGoodsVo> getGoods(Page<Goods> goodsPage) {
        return (List<OutOfStockGoodsVo>) ConvertUtils.convertCollection(goodsPage.getRecords(), OutOfStockGoodsVo::new, (s, t) -> {
            Category category = categoryMapper.selectById(s.getCategoryId());
            t.setCategoryName(category.getName());
            if (category.getParentId() != 0) {
                Category parentCategory = categoryMapper.selectById(category.getParentId());
                t.setCategoryPName(parentCategory.getName());
            }
            t.setIsOnSale(s.getIsOnSale() == 1);
        }).orElseThrow();
    }

    public void updateSaleStatus(Integer id, String status) {
        goodsMapper.update(Wrappers.lambdaUpdate(Goods.class)
                .eq(Goods::getId, id)
                .set(Goods::getIsOnSale, "true".equals(status) ? 1 : 0)
        );
    }

    public GoodsInfoAdminVo getGoodsInfo(Integer id) {
        // 获取商品信息
        Goods goods = goodsMapper.selectById(id);

        // 获取商品分类信息
        Integer categoryId = goods.getCategoryId();
        Category category = categoryMapper.selectById(categoryId);
        Category parentCategory = categoryMapper.selectById(category.getParentId());

        // 分类数据
        List<Integer> cateData = new ArrayList<>();
        cateData.add(parentCategory.getId());
        cateData.add(category.getId());

        // 获取商品关联的产品信息
//        List<Product> productInfo = productMapper.selectList(
//                Wrappers.lambdaQuery(Product.class)
//                        .eq(Product::getGoodsId, id)
//        );

        // 将商品信息封装到VO对象中
        GoodsInfoAdminVo goodsInfoVo = new GoodsInfoAdminVo();
        goodsInfoVo.setInfo(goods);
        goodsInfoVo.setCateData(cateData);

        return goodsInfoVo;
    }

    public List<CategoryOptionVo> getAllCategory() {
        // 查询一级分类
        List<Category> categoryL1 = categoryMapper.selectList(
                Wrappers.lambdaQuery(Category.class)
                        .eq(Category::getIsShow, 1)
                        .eq(Category::getLevel, "L1")
        );

        // 查询二级分类
        List<Category> categoryL2 = categoryMapper.selectList(
                Wrappers.lambdaQuery(Category.class)
                        .eq(Category::getIsShow, 1)
                        .eq(Category::getLevel, "L2")
        );

        // 将数据合并成符合要求的格式
        List<CategoryOptionVo> newData = new ArrayList<>();
        for (Category item : categoryL1) {
            List<CategoryOptionVo> children = new ArrayList<>();
            for (Category cItem : categoryL2) {
                if (cItem.getParentId().equals(item.getId())) {
                    CategoryOptionVo child = new CategoryOptionVo();
                    child.setValue(cItem.getId());
                    child.setLabel(cItem.getName());
                    children.add(child);
                }
            }
            CategoryOptionVo categoryOption = new CategoryOptionVo();
            categoryOption.setValue(item.getId());
            categoryOption.setLabel(item.getName());
            categoryOption.setChildren(children);
            newData.add(categoryOption);
        }

        return newData;
    }

    public List<Goods> getGoodsSnName(Integer cateId) {
        // 按照 goods_sn 排序
        return goodsMapper.selectList(
                        Wrappers.lambdaQuery(Goods.class)
                                .eq(Goods::getCategoryId, cateId)
                                .eq(Goods::getIsDelete, 0)
//                                .orderByDesc(Goods::getGoodsSn)
                );
    }

//    @Transactional(rollbackFor = Exception.class)
//    public void store(GoodsStoreDto dto) {
//        cartMapper.update(Wrappers.lambdaUpdate(Cart.class)
//                .eq(Cart::getGoodsId, dto.getInfo().getId())
//                .set(Cart::getListPicUrl, dto.getInfo().getListPicUrl())
//        );
//        Integer id = dto.getInfo().getId();
//        if (id != null && id > 0) {
//            goodsMapper.updateById(dto.getInfo());
//        } else {
//            Goods info = dto.getInfo();
//            info.setId(null);
//            goodsMapper.insert(info);
//        }
//    }
}
