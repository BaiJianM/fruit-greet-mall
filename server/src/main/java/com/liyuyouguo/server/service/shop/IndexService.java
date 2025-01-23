package com.liyuyouguo.server.service.shop;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liyuyouguo.server.beans.vo.shop.AppInfoVo;
import com.liyuyouguo.server.beans.vo.shop.CategoryVo;
import com.liyuyouguo.server.commons.FruitShopException;
import com.liyuyouguo.server.commons.ShopError;
import com.liyuyouguo.server.entity.shop.*;
import com.liyuyouguo.server.mapper.*;
import com.liyuyouguo.server.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 首页服务类
 *
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IndexService {

    private final AdMapper adMapper;

    private final NoticeMapper noticeMapper;

    private final CategoryMapper categoryMapper;

    private final GoodsMapper goodsMapper;

    private final CartMapper cartMapper;

    /**
     * 获取小程序信息
     */
    public AppInfoVo getAppInfo() {
        List<Ad> banner = adMapper.selectList(Wrappers.lambdaQuery(Ad.class)
                .eq(Ad::getEnabled, 1)
                .eq(Ad::getIsDelete, 0)
                .orderByAsc(Ad::getSortOrder));
        List<Notice> notices = noticeMapper.selectList(Wrappers.lambdaQuery(Notice.class)
                .eq(Notice::getIsDelete, 0));
        List<Category> channels = categoryMapper.selectList(Wrappers.lambdaQuery(Category.class)
                .eq(Category::getIsChannel, 1)
                .eq(Category::getParentId, 0)
                .orderByAsc(Category::getSortOrder));
        List<Category> categories = categoryMapper.selectList(Wrappers.lambdaQuery(Category.class)
                .eq(Category::getIsShow, 1)
                .eq(Category::getParentId, 0)
                .orderByAsc(Category::getSortOrder));
        List<CategoryVo> categoryVos = (List<CategoryVo>) ConvertUtils.convertCollection(categories, CategoryVo::new)
                .orElseThrow(() -> new FruitShopException(ShopError.INDEX_ERROR));
        categoryVos.forEach(categoryVo -> {
            List<Goods> goods = goodsMapper.selectList(Wrappers.lambdaQuery(Goods.class)
                    .eq(Goods::getCategoryId, categoryVo.getId())
                    .ge(Goods::getGoodsNumber, 0)
                    .eq(Goods::getIsOnSale, 1)
                    .eq(Goods::getIsIndex, 1)
                    .eq(Goods::getIsDelete, 0)
                    .orderByAsc(Goods::getSortOrder));
            categoryVo.setGoodsList(goods);
        });
        // TODO 这里少一个从token获取登录人id的操作
        Long userId = 1048L;
        List<Cart> carts = cartMapper.selectList(Wrappers.lambdaQuery(Cart.class)
                .eq(Cart::getUserId, userId)
                .eq(Cart::getIsDelete, 0));
        long cartCount = carts.stream().mapToLong(Cart::getNumber).sum();
        AppInfoVo appInfoVo = new AppInfoVo();
        appInfoVo.setChannel(channels);
        appInfoVo.setBanner(banner);
        appInfoVo.setNotice(notices);
        appInfoVo.setCategoryList(categoryVos);
        appInfoVo.setCartCount(cartCount);
        return appInfoVo;
    }

}
