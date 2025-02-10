package com.liyuyouguo.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.liyuyouguo.admin.service.ShopCartService;
import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.beans.vo.ShopCartVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import com.liyuyouguo.common.entity.shop.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author baijianmin
 */
@Slf4j
@FruitGreetController("/shopcart")
@RequiredArgsConstructor
@Validated
public class ShopCartController {

    private final ShopCartService shopCartService;

    @GetMapping
    public FruitGreetResponse<PageResult<ShopCartVo>> index(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "name", defaultValue = "") String name) {
        return FruitGreetResponse.success(shopCartService.getCartPage(page, size, name));
    }

}
