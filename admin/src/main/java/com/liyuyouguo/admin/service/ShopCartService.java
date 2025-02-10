package com.liyuyouguo.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.beans.vo.ShopCartVo;
import com.liyuyouguo.common.entity.shop.Cart;
import com.liyuyouguo.common.entity.shop.User;
import com.liyuyouguo.common.mapper.CartMapper;
import com.liyuyouguo.common.mapper.UserMapper;
import com.liyuyouguo.common.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

/**
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShopCartService {

    private final CartMapper cartMapper;

    private final UserMapper userMapper;

    public PageResult<ShopCartVo> getCartPage(int page, int size, String name) {
        // 执行分页查询
        Page<Cart> cartData = cartMapper.selectPage(new Page<>(page, size),
                Wrappers.lambdaQuery(Cart.class).like(Cart::getGoodsName, name).orderByDesc(Cart::getId));

        List<ShopCartVo> shopCarts = (List<ShopCartVo>) ConvertUtils.convertCollection(cartData.getRecords(), ShopCartVo::new, (s, t) -> {
            // 查询用户信息
            User userInfo = userMapper.selectById(s.getUserId());
            if (userInfo != null) {
                // 解码用户昵称
                t.setNickname(new String(Base64.getDecoder().decode(userInfo.getNickname())));
            } else {
                t.setNickname("已删除");
            }
        }).orElseThrow();

        return ConvertUtils.convert(cartData, PageResult<ShopCartVo>::new, (s, t) -> t.setRecords(shopCarts)).orElseThrow();
    }

}
