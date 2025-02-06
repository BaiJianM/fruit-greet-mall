package com.liyuyouguo.server.controller;

import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.dto.shop.CartAddDto;
import com.liyuyouguo.common.beans.dto.shop.CartCheckedDto;
import com.liyuyouguo.common.beans.dto.shop.CartDeleteDto;
import com.liyuyouguo.common.beans.dto.shop.CartUpdateDto;
import com.liyuyouguo.common.beans.vo.CartCheckoutVo;
import com.liyuyouguo.common.beans.vo.CartCountVo;
import com.liyuyouguo.common.beans.vo.CartInfoVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import com.liyuyouguo.server.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 购物车控制层
 *
 * @author baijianmin
 */
@Slf4j
@FruitGreetController("/cart")
@RequiredArgsConstructor
@Validated
public class CartController {

    private final CartService cartService;

    /**
     * 获取购物车商品的总件件数
     *
     * @return CartCountVo 购物车件数
     */
    @GetMapping("/goodsCount")
    public FruitGreetResponse<CartCountVo> getGoodsCount() {
        return FruitGreetResponse.success(cartService.getGoodsCount());
    }

    /**
     * 获取购物车信息，所有对购物车的增删改操作，都要重新返回购物车的信息
     *
     * @return CartInfoVo 购物车信息
     */
    @GetMapping("/index")
    public FruitGreetResponse<CartInfoVo> getIndex() {
        return FruitGreetResponse.success(cartService.getIndex());
    }

    /**
     * 添加商品到购物车
     *
     * @param dto 传参
     * @return CartInfoVo 当前购物车信息
     */
    @PostMapping("/add")
    public FruitGreetResponse<CartInfoVo> add(@RequestBody CartAddDto dto) {
        return FruitGreetResponse.success(cartService.add(dto));
    }

    /**
     * 更新指定的购物车信息
     *
     * @param dto 传参
     * @return CartInfoVo 当前购物车信息
     */
    @PostMapping("/update")
    public FruitGreetResponse<CartInfoVo> update(@RequestBody CartUpdateDto dto) {
        return FruitGreetResponse.success(cartService.update(dto));
    }

    /**
     * 是否选择商品，如果已经选择，则取消选择，批量操作
     *
     * @param dto 传参
     * @return CartInfoVo 当前购物车信息
     */
    @PostMapping("/checked")
    public FruitGreetResponse<CartInfoVo> checked(@Valid @RequestBody CartCheckedDto dto) {
        return FruitGreetResponse.success(cartService.checked(dto));
    }

    /**
     * 删除选中的购物车商品
     *
     * @param dto 传参
     * @return CartInfoVo 当前购物车信息
     */
    @PostMapping("/delete")
    public FruitGreetResponse<CartInfoVo> delete(@Valid @RequestBody CartDeleteDto dto) {
        return FruitGreetResponse.success(cartService.delete(dto));
    }

    /**
     * 订单提交前的检验和填写相关订单信息
     *
     * @param orderId   订单id
     * @param type      是否团购
     * @param addressId 收货地址id
     * @param addType   添加商品的类型
     * @return CartCheckoutVo 订单提交前的检验和填写相关订单信息
     */
    @GetMapping("/checkout")
    public FruitGreetResponse<CartCheckoutVo> checkout(@RequestParam("orderFrom") Integer orderId,
                                                       @RequestParam("type") Integer type,
                                                       @RequestParam("addressId") Integer addressId,
                                                       @RequestParam("addType") Integer addType) {
        return FruitGreetResponse.success(cartService.checkout(orderId, type, addressId, addType));
    }

}
