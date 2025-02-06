package com.liyuyouguo.server.controller;

import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.beans.dto.shop.OrderOperateDto;
import com.liyuyouguo.common.beans.dto.shop.OrderQueryDto;
import com.liyuyouguo.common.beans.dto.shop.OrderSubmitDto;
import com.liyuyouguo.common.beans.dto.shop.OrderUpdateDto;
import com.liyuyouguo.common.beans.vo.OrderCountVo;
import com.liyuyouguo.common.beans.vo.OrderDetailVo;
import com.liyuyouguo.common.beans.vo.OrderGoodsVo;
import com.liyuyouguo.common.beans.vo.OrderVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
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
@FruitGreetController("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 我的页面获取订单数状态
     *
     * @return OrderCountVo 订单数
     */
    @GetMapping("/orderCount")
    public FruitGreetResponse<OrderCountVo> getOrderStatusCount() {
        return FruitGreetResponse.success(orderService.getOrderStatusCount());
    }

    /**
     * 获取订单列表
     *
     * @param dto 传参
     * @return PageResult<OrderVo> 订单信息分页
     */
    @PostMapping("/list")
    public FruitGreetResponse<PageResult<OrderVo>> getOrderList(@RequestBody OrderQueryDto dto) {
        return FruitGreetResponse.success(orderService.getOrderList(dto));
    }

    /**
     * 获取订单数量
     *
     * @return Long 订单数量
     */
    @GetMapping("/count")
    public FruitGreetResponse<Long> getOrderCount(@RequestParam("showType") Integer showType) {
        return FruitGreetResponse.success(orderService.getOrderCount(showType));
    }

    /**
     * 获取订单详情
     *
     * @param orderId 订单id
     * @return OrderDetailVo 订单详情
     */
    @GetMapping("/detail")
    public FruitGreetResponse<OrderDetailVo> getOrderDetail(@RequestParam Integer orderId) {
        return FruitGreetResponse.success(orderService.getOrderDetail(orderId));
    }

    /**
     * 获取checkout页面的商品列表
     *
     * @param orderId 订单id
     * @return List<OrderGoodsVo> 商品列表
     */
    @GetMapping("/orderGoods")
    public FruitGreetResponse<List<OrderGoodsVo>> getOrderGoods(@RequestParam(required = false) Integer orderId) {
        return FruitGreetResponse.success(orderService.getOrderGoods(orderId));
    }

    /**
     * 取消订单
     *
     * @param dto 订单id
     * @return Integer 取消订单结果
     */
    @PostMapping("/cancel")
    public FruitGreetResponse<Integer> cancel(@RequestBody OrderOperateDto dto) {
        return FruitGreetResponse.success(orderService.cancel(dto.getOrderId()));
    }

    /**
     * 删除订单
     *
     * @param dto 订单id
     * @return Integer 删除订单结果
     */
    @PostMapping("/delete")
    public FruitGreetResponse<Integer> delete(@RequestBody OrderOperateDto dto) {
        return FruitGreetResponse.success(orderService.delete(dto.getOrderId()));
    }

    /**
     * 确认订单
     *
     * @param dto 订单id
     * @return Integer 确认订单结果
     */
    @PostMapping("/confirm")
    public FruitGreetResponse<Integer> confirm(@RequestBody OrderOperateDto dto) {
        return FruitGreetResponse.success(orderService.confirm(dto.getOrderId()));
    }

    /**
     * 完成评论后的订单
     *
     * @param orderId 订单id
     * @return Integer 完成评论结果
     */
    @GetMapping("/complete")
    public FruitGreetResponse<Integer> complete(@RequestParam Integer orderId) {
        return FruitGreetResponse.success(orderService.complete(orderId));
    }

    /**
     * 提交订单
     *
     * @param dto 传参
     * @return Order 订单信息
     */
    @PostMapping("/submit")
    public FruitGreetResponse<Order> submit(@RequestBody OrderSubmitDto dto) {
        return FruitGreetResponse.success(orderService.submit(dto));
    }

    /**
     * 更新订单
     *
     * @param dto 传参
     * @return Integer 更新订单结果
     */
    @PostMapping("/update")
    public FruitGreetResponse<Integer> update(@RequestBody OrderUpdateDto dto) {
        return FruitGreetResponse.success(orderService.update(dto));
    }

    /**
     * 查询物流信息asd
     *
     * @param orderId 订单id
     * @return OrderExpress 订单物流信息
     */
    @GetMapping("/express")
    public FruitGreetResponse<OrderExpress> getExpress(@RequestParam Integer orderId) {
        return FruitGreetResponse.success(orderService.getExpress(orderId));
    }

}
