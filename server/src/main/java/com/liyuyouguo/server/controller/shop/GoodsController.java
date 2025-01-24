package com.liyuyouguo.server.controller.shop;

import com.liyuyouguo.common.annotations.FruitShopController;
import com.liyuyouguo.common.beans.vo.shop.GoodsInfoVo;
import com.liyuyouguo.common.commons.FruitShopResponse;
import com.liyuyouguo.common.entity.shop.Goods;
import com.liyuyouguo.server.service.GoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 商品控制层
 *
 * @author baijianmin
 */
@Slf4j
@FruitShopController("/goods")
@RequiredArgsConstructor
public class GoodsController {

    private final GoodsService goodsService;

    /**
     * 获取商品详情
     *
     * @param goodsId 商品id
     * @return GoodsInfoVo 商品信息
     */
    @GetMapping("/detail")
    public FruitShopResponse<GoodsInfoVo> getGoodsDetail(@RequestParam("id") Integer goodsId) {
        return FruitShopResponse.success(goodsService.getGoodsDetail(goodsId));
    }

    /**
     * 在售的商品总数
     *
     * @return Integer 在售的商品总数
     */
    @GetMapping("/count")
    public FruitShopResponse<Integer> getGoodsCount() {
        return FruitShopResponse.success(goodsService.getGoodsCount());
    }

    /**
     * 获取所有商品列表
     *
     * @return List<Goods> 商品列表
     */
    @GetMapping("/index")
    public FruitShopResponse<List<Goods>> getAllGoodsList() {
        return FruitShopResponse.success(goodsService.getAllGoodsList());
    }

    /**
     * 根据商品id获取商品信息
     *
     * @param goodsId 商品id
     * @return Goods 商品信息
     */
    @GetMapping("/goodsShare")
    public FruitShopResponse<Goods> getGoodsById(@RequestParam("id") Integer goodsId) {
        return FruitShopResponse.success(goodsService.getGoodsById(goodsId));
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
    @GetMapping("/list")
    public FruitShopResponse<List<Goods>> getGoodsList(@RequestParam("keyword") String keyword, @RequestParam("sort") String sort,
                                                       @RequestParam("order") String order, @RequestParam("sales") String sales) {
        return FruitShopResponse.success(goodsService.getGoodsList(keyword, sort, order, sales));
    }

}
