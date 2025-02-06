package com.liyuyouguo.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyuyouguo.common.beans.vo.GoodsInfoVo;
import com.liyuyouguo.common.beans.vo.ProductInfoVo;
import com.liyuyouguo.common.commons.FruitGreetException;
import com.liyuyouguo.common.commons.FruitGreetError;
import com.liyuyouguo.common.entity.shop.Goods;
import com.liyuyouguo.common.entity.shop.GoodsGallery;
import com.liyuyouguo.common.entity.shop.Product;
import com.liyuyouguo.common.entity.shop.SearchHistory;
import com.liyuyouguo.common.mapper.GoodsGalleryMapper;
import com.liyuyouguo.common.mapper.GoodsMapper;
import com.liyuyouguo.common.mapper.SearchHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品服务类
 *
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsService {

    private final GoodsMapper goodsMapper;

    private final GoodsGalleryMapper galleryMapper;

    private final FootPrintService footPrintService;

    private final ProductService productService;

    private final SearchHistoryMapper searchHistoryMapper;

    public GoodsInfoVo getGoodsDetail(Integer goodsId) {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        // 获取商品
        Goods goods = goodsMapper.selectOne(Wrappers.lambdaQuery(Goods.class)
                .eq(Goods::getId, goodsId)
                .eq(Goods::getIsDelete, 0));
        if (goods == null) {
            throw new FruitGreetException(FruitGreetError.GOODS_NOT_EXIST);
        }
        List<GoodsGallery> goodsGalleries = galleryMapper.selectPage(new Page<>(1, 6), Wrappers.lambdaQuery(GoodsGallery.class)
                .eq(GoodsGallery::getGoodsId, goodsId)
                .eq(GoodsGallery::getIsDelete, 0)
                .orderByAsc(GoodsGallery::getSortOrder)).getRecords();
        // 新增或更新用户足迹
        footPrintService.saveFootPrint(userId, goodsId);
        ProductInfoVo productInfoVo = productService.getSpecificationList(goodsId);
        List<Product> products = productService.getProductList(goodsId);
        int goodsNumber = products.stream().filter(p -> p.getGoodsNumber() > 0).mapToInt(Product::getGoodsNumber).sum();
        goods.setGoodsNumber(goodsNumber);

        GoodsInfoVo goodsInfoVo = new GoodsInfoVo();
        goodsInfoVo.setInfo(goods);
        goodsInfoVo.setGallery(goodsGalleries);
        goodsInfoVo.setSpecificationList(productInfoVo);
        goodsInfoVo.setProductList(products);
        return goodsInfoVo;
    }

    /**
     * 在售的商品总数
     *
     * @return Integer 在售的商品总数
     */
    public Integer getGoodsCount() {
        List<Goods> goods = goodsMapper.selectList(Wrappers.lambdaQuery(Goods.class)
                .eq(Goods::getIsOnSale, 1)
                .eq(Goods::getIsDelete, 0));
        return goods.size();
    }

    /**
     * 获取所有商品列表
     *
     * @return List<Goods> 商品列表
     */
    public List<Goods> getAllGoodsList() {
        return goodsMapper.selectList(null);
    }

    /**
     * 根据商品id获取商品信息
     *
     * @param goodsId 商品id
     * @return Goods 商品信息
     */
    public Goods getGoodsById(Integer goodsId) {
        return goodsMapper.selectById(goodsId);
    }

    /**
     * 获取商品列表
     *
     * @param keyword 关键字
     * @param sort    排序类型
     * @param order   排序字段
     * @param sales   销量
     * @return List<Goods> 商品列表
     */
    public List<Goods> getGoodsList(String keyword, String sort, String order, String sales) {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        // 查询条件
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_on_sale", 1).eq("is_delete", 0);

        // 如果关键字不为空，添加搜索条件
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.like("name", keyword);

            // 添加到搜索历史
            SearchHistory searchHistory = new SearchHistory();
            searchHistory.setKeyword(keyword);
            searchHistory.setUserId(userId);
            searchHistory.setAddTime(LocalDateTime.now());
            searchHistoryMapper.insert(searchHistory);

            // TODO: 添加对搜索记录统计的逻辑，判断搜索次数并存储为热门关键字
        }
        // 排序条件
        if ("price".equals(sort)) {
            // 按价格排序
            if ("asc".equalsIgnoreCase(order)) {
                queryWrapper.orderByAsc("retail_price");
            } else {
                queryWrapper.orderByDesc("retail_price");
            }
        } else if ("sales".equals(sort)) {
            // 按销量排序
            if ("asc".equalsIgnoreCase(sales)) {
                queryWrapper.orderByAsc("sell_volume");
            } else {
                queryWrapper.orderByDesc("sell_volume");
            }
        } else {
            // 按商品添加时间排序
            queryWrapper.orderByAsc("sort_order");
        }
        // 查询商品数据
        return goodsMapper.selectList(queryWrapper);
    }
}
