package com.liyuyouguo.server.controller.shop;

import com.liyuyouguo.common.annotations.FruitShopController;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.beans.dto.shop.OrderOperateDto;
import com.liyuyouguo.common.beans.dto.shop.OrderQueryDto;
import com.liyuyouguo.common.beans.dto.shop.OrderSubmitDto;
import com.liyuyouguo.common.beans.dto.shop.OrderUpdateDto;
import com.liyuyouguo.common.beans.vo.shop.OrderCountVo;
import com.liyuyouguo.common.beans.vo.shop.OrderDetailVo;
import com.liyuyouguo.common.beans.vo.shop.OrderGoodsVo;
import com.liyuyouguo.common.beans.vo.shop.OrderVo;
import com.liyuyouguo.common.commons.FruitShopResponse;
import com.liyuyouguo.common.entity.shop.Order;
import com.liyuyouguo.common.entity.shop.OrderExpress;
import com.liyuyouguo.server.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 订单控制层
 *
 * @author baijianmin
 */
@Slf4j
@FruitShopController("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 我的页面获取订单数状态
     *
     * @return OrderCountVo 订单数
     */
    @GetMapping("/orderCount")
    public FruitShopResponse<OrderCountVo> getOrderStatusCount() {
        return FruitShopResponse.success(orderService.getOrderStatusCount());
    }

    /**
     * 获取订单列表
     *
     * @param dto 传参
     * @return PageResult<OrderVo> 订单信息分页
     */
    @PostMapping("/list")
    public FruitShopResponse<PageResult<OrderVo>> getOrderList(@RequestBody OrderQueryDto dto) {
        return FruitShopResponse.success(orderService.getOrderList(dto));
    }

    /**
     * 获取订单数量
     *
     * @return Long 订单数量
     */
    @GetMapping("/count")
    public FruitShopResponse<Long> getOrderCount(@RequestParam("showType") Integer showType) {
        return FruitShopResponse.success(orderService.getOrderCount(showType));
    }

    /**
     * 获取订单详情
     *
     * @param orderId 订单id
     * @return OrderDetailVo 订单详情
     */
    @GetMapping("/detail")
    public FruitShopResponse<OrderDetailVo> getOrderDetail(@RequestParam Integer orderId) {
        return FruitShopResponse.success(orderService.getOrderDetail(orderId));
    }

    /**
     * 获取checkout页面的商品列表
     *
     * @param orderId 订单id
     * @return List<OrderGoodsVo> 商品列表
     */
    @GetMapping("/orderGoods")
    public FruitShopResponse<List<OrderGoodsVo>> getOrderGoods(@RequestParam(required = false) Integer orderId) {
        return FruitShopResponse.success(orderService.getOrderGoods(orderId));
    }

    /**
     * 取消订单
     *
     * @param dto 订单id
     * @return Integer 取消订单结果
     */
    @PostMapping("/cancel")
    public FruitShopResponse<Integer> cancel(@RequestBody OrderOperateDto dto) {
        return FruitShopResponse.success(orderService.cancel(dto.getOrderId()));
    }

    /**
     * 删除订单
     *
     * @param dto 订单id
     * @return Integer 删除订单结果
     */
    @PostMapping("/delete")
    public FruitShopResponse<Integer> delete(@RequestBody OrderOperateDto dto) {
        return FruitShopResponse.success(orderService.delete(dto.getOrderId()));
    }

    /**
     * 确认订单
     *
     * @param dto 订单id
     * @return Integer 确认订单结果
     */
    @PostMapping("/confirm")
    public FruitShopResponse<Integer> confirm(@RequestBody OrderOperateDto dto) {
        return FruitShopResponse.success(orderService.confirm(dto.getOrderId()));
    }

    /**
     * 完成评论后的订单
     *
     * @param orderId 订单id
     * @return Integer 完成评论结果
     */
    @GetMapping("/complete")
    public FruitShopResponse<Integer> complete(@RequestParam Integer orderId) {
        return FruitShopResponse.success(orderService.complete(orderId));
    }

    /**
     * 提交订单
     *
     * @param dto 传参
     * @return Order 订单信息
     */
    @PostMapping("/submit")
    public FruitShopResponse<Order> submit(@RequestBody OrderSubmitDto dto) {
        return FruitShopResponse.success(orderService.submit(dto));
    }

    /**
     * 更新订单
     *
     * @param dto 传参
     * @return Integer 更新订单结果
     */
    @PostMapping("/update")
    public FruitShopResponse<Integer> update(@RequestBody OrderUpdateDto dto) {
        return FruitShopResponse.success(orderService.update(dto));
    }

    /**
     * 查询物流信息asd
     *
     * @param orderId 订单id
     * @return OrderExpress 订单物流信息
     */
    @GetMapping("/express")
    public FruitShopResponse<OrderExpress> getExpress(@RequestParam Integer orderId) {
        return FruitShopResponse.success(orderService.getExpress(orderId));
    }

}
