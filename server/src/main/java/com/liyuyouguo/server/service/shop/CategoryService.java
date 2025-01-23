package com.liyuyouguo.server.service.shop;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyuyouguo.server.beans.PageResult;
import com.liyuyouguo.server.beans.dto.shop.CatalogQueryDto;
import com.liyuyouguo.server.entity.shop.Category;
import com.liyuyouguo.server.entity.shop.Goods;
import com.liyuyouguo.server.mapper.CategoryMapper;
import com.liyuyouguo.server.mapper.GoodsMapper;
import com.liyuyouguo.server.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 商品分类服务类
 *
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    private final GoodsMapper goodsMapper;

    /**
     * 获取商品目录
     *
     * @param categoryId 分类id
     * @return List<Category> 目录列表
     */
    public List<Category> getCatalog(Integer categoryId) {
        List<Category> records = categoryMapper.selectPage(new Page<>(1, 10), Wrappers.lambdaQuery(Category.class)
                .eq(Category::getParentId, 0)
                .eq(Category::getIsCategory, 1)
                .orderByAsc(Category::getSortOrder)).getRecords();
        return records == null ? Collections.emptyList() : records;
    }

    /**
     * 获取当前目录下的商品列表
     *
     * @param dto 查询参数
     * @return List<Goods> 商品列表
     */
    public PageResult<Goods> getCurrentCatalog(CatalogQueryDto dto) {
        Integer categoryId = dto.getId();
        Page<Goods> page = new Page<>();
        page.setCurrent(dto.getCurrent());
        page.setSize(dto.getSize());
        Page<Goods> goodsPage;
        if (categoryId == 0) {
            goodsPage = goodsMapper.selectPage(page, Wrappers.lambdaQuery(Goods.class)
                    .eq(Goods::getIsOnSale, 1)
                    .eq(Goods::getIsDelete, 0)
                    .orderByAsc(Goods::getSortOrder));
        } else {
            goodsPage = goodsMapper.selectPage(page, Wrappers.lambdaQuery(Goods.class)
                    .eq(Goods::getIsOnSale, 1)
                    .eq(Goods::getIsDelete, 0)
                            .eq(Goods::getCategoryId, categoryId)
                    .orderByAsc(Goods::getSortOrder));
        }
        return ConvertUtils.convert(goodsPage, PageResult<Goods>::new).orElseThrow();
    }

    /**
     * 获取选中目录
     *
     * @param categoryId 分类id
     * @return Category 分类信息
     */
    public Category getCurrentCatalogById(Integer categoryId) {
        return categoryMapper.selectById(categoryId);
    }
}
