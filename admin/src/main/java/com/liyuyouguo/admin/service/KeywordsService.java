package com.liyuyouguo.admin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.entity.shop.Cart;
import com.liyuyouguo.common.mapper.CartMapper;
import com.liyuyouguo.common.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordsService {

    private final CartMapper cartMapper;

    public PageResult<Cart> getCartList(int page, int size, String name) {
        Page<Cart> cartPage = cartMapper.selectPage(new Page<>(page, size), Wrappers.lambdaQuery(Cart.class)
                .like(Cart::getGoodsName, "%" + name + "%")
                .orderByDesc(Cart::getId));
        return ConvertUtils.convert(cartPage, PageResult<Cart>::new).orElseThrow();
    }

}
