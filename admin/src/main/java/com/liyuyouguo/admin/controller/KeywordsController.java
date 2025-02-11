package com.liyuyouguo.admin.controller;

import com.liyuyouguo.admin.service.KeywordsService;
import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.PageResult;
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
@FruitGreetController("/keywords")
@RequiredArgsConstructor
@Validated
public class KeywordsController {

    private final KeywordsService keywordsService;

    @GetMapping
    public FruitGreetResponse<PageResult<Cart>> index(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String name) {
        return FruitGreetResponse.success(keywordsService.getCartList(page, size, name));
    }

}
