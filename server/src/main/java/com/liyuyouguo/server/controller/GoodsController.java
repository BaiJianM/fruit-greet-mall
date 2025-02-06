package com.liyuyouguo.server.controller;

import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.vo.GoodsInfoVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
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
@FruitGreetController("/goods")
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
    public FruitGreetResponse<GoodsInfoVo> getGoodsDetail(@RequestParam("id") Integer goodsId) {
        return FruitGreetResponse.success(goodsService.getGoodsDetail(goodsId));
    }

    /**
     * 在售的商品总数
     *
     * @return Integer 在售的商品总数
     */
    @GetMapping("/count")
    public FruitGreetResponse<Integer> getGoodsCount() {
        return FruitGreetResponse.success(goodsService.getGoodsCount());
    }

    /**
     * 获取所有商品列表
     *
     * @return List<Goods> 商品列表
     */
    @GetMapping("/index")
    public FruitGreetResponse<List<Goods>> getAllGoodsList() {
        return FruitGreetResponse.success(goodsService.getAllGoodsList());
    }

    /**
     * 根据商品id获取商品信息
     *
     * @param goodsId 商品id
     * @return Goods 商品信息
     */
    @GetMapping("/goodsShare")
    public FruitGreetResponse<Goods> getGoodsById(@RequestParam("id") Integer goodsId) {
        return FruitGreetResponse.success(goodsService.getGoodsById(goodsId));
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
    public FruitGreetResponse<List<Goods>> getGoodsList(@RequestParam("keyword") String keyword, @RequestParam("sort") String sort,
                                                        @RequestParam("order") String order, @RequestParam("sales") String sales) {
        return FruitGreetResponse.success(goodsService.getGoodsList(keyword, sort, order, sales));
    }

}
