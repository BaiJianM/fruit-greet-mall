package com.liyuyouguo.admin.controller;

import com.liyuyouguo.admin.service.OrderService;
import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.beans.dto.shop.OrderPriceUpdateDto;
import com.liyuyouguo.common.beans.dto.shop.OrderQueryAdminDto;
import com.liyuyouguo.common.beans.dto.shop.SaveAdminMemoDto;
import com.liyuyouguo.common.beans.dto.shop.SaveGoodsListDto;
import com.liyuyouguo.common.beans.vo.OrderAdminVo;
import com.liyuyouguo.common.beans.vo.OrderDetailAdminVo;
import com.liyuyouguo.common.beans.vo.RegionAdminVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *
 *
 * @author baijianmin
 */
@Slf4j
@FruitGreetController("/order")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public FruitGreetResponse<PageResult<OrderAdminVo>> getOrderList(OrderQueryAdminDto dto) {
        return FruitGreetResponse.success(orderService.getOrderList(dto));
    }

    @GetMapping("/getAutoStatus")
    public FruitGreetResponse<Integer> getAutoStatus() {
        return FruitGreetResponse.success(orderService.getAutoStatus());
    }

    @GetMapping("/toDelivery")
    public FruitGreetResponse<PageResult<OrderAdminVo>> toDelivery(@RequestParam(value = "page", defaultValue = "1") int page,
                                                              @RequestParam(value = "size", defaultValue = "10") int size,
                                                              @RequestParam(value = "status", defaultValue = "") String status) {
        return FruitGreetResponse.success(orderService.toDelivery(page, size, status));
    }

    @PostMapping("/saveGoodsList")
    public FruitGreetResponse<String> saveGoodsList(@Valid @RequestBody SaveGoodsListDto dto) {
        return FruitGreetResponse.success(orderService.saveGoodsList(dto));
    }

    @PostMapping("/goodsListDelete")
    public FruitGreetResponse<String> goodsListDelete(@Valid @RequestBody SaveGoodsListDto dto) {
        return FruitGreetResponse.success(orderService.goodsListDelete(dto));
    }

    @PostMapping("/saveAdminMemo")
    public FruitGreetResponse<Integer> saveAdminMemo(@Valid @RequestBody SaveAdminMemoDto dto) {
        return FruitGreetResponse.success(orderService.saveAdminMemo(dto));
    }

    @PostMapping("/savePrintInfo")
    public FruitGreetResponse<Integer> savePrintInfo(@Valid @RequestBody SaveAdminMemoDto dto) {
        return FruitGreetResponse.success(orderService.savePrintInfo(dto));
    }

    @PostMapping("/saveExpressValueInfo")
    public FruitGreetResponse<Integer> saveExpressValueInfo(@Valid @RequestBody SaveAdminMemoDto dto) {
        return FruitGreetResponse.success(orderService.saveExpressValueInfo(dto));
    }

    @PostMapping("/saveRemarkInfo")
    public FruitGreetResponse<Integer> saveRemarkInfo(@Valid @RequestBody SaveAdminMemoDto dto) {
        return FruitGreetResponse.success(orderService.saveRemarkInfo(dto));
    }

    @GetMapping("/detail")
    public FruitGreetResponse<OrderDetailAdminVo> detail(@RequestParam("orderId") Integer orderId) {
        return FruitGreetResponse.success(orderService.getOrderDetail(orderId));
    }

    @GetMapping("/getAllRegion")
    public FruitGreetResponse<List<RegionAdminVo>> getAllRegion() {
        return FruitGreetResponse.success(orderService.getAllRegions());
    }

    @PostMapping("/orderpack")
    public FruitGreetResponse<Void> orderPack(@RequestParam Integer orderId) {
        orderService.updateOrderStatusToPacked(orderId);
        return FruitGreetResponse.success();
    }

    @PostMapping("/orderReceive")
    public FruitGreetResponse<Void> orderReceive(@RequestParam Integer orderId) {
        orderService.updateOrderStatusToReceived(orderId);
        return FruitGreetResponse.success();
    }

    @PostMapping("/orderPrice")
    public FruitGreetResponse<Void> updateOrderPrice(@RequestBody OrderPriceUpdateDto dto) {
        orderService.updateOrderPrice(dto);
        return FruitGreetResponse.success();
    }

}
