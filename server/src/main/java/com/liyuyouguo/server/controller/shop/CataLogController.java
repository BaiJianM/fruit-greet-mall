package com.liyuyouguo.server.controller.shop;

import com.liyuyouguo.server.annotations.FruitShopController;
import com.liyuyouguo.server.beans.PageResult;
import com.liyuyouguo.server.beans.dto.shop.CatalogQueryDto;
import com.liyuyouguo.server.commons.FruitShopResponse;
import com.liyuyouguo.server.entity.shop.Category;
import com.liyuyouguo.server.entity.shop.Goods;
import com.liyuyouguo.server.service.shop.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 商品目录控制层
 *
 * @author baijianmin
 */
@Slf4j
@FruitShopController("/catalog")
@RequiredArgsConstructor
public class CataLogController {

    private final CategoryService categoryService;

    /**
     * 获取商品目录
     *
     * @param categoryId 分类id
     * @return List<Category> 目录列表
     */
    @GetMapping("/index")
    public FruitShopResponse<List<Category>> getCatalog(@RequestParam(value = "id", required = false) Integer categoryId) {
        return FruitShopResponse.success(categoryService.getCatalog(categoryId));
    }

    /**
     * 获取当前目录下的商品列表
     *
     * @param dto 查询参数
     * @return PageResult<Goods> 商品列表
     */
    @PostMapping("/currentlist")
    public FruitShopResponse<PageResult<Goods>> getCurrentCatalog(@RequestBody CatalogQueryDto dto) {
        return FruitShopResponse.success(categoryService.getCurrentCatalog(dto));
    }

    /**
     * 获取选中目录
     *
     * @param categoryId 分类id
     * @return Category 分类信息
     */
    @GetMapping("/current")
    public FruitShopResponse<Category> getCurrentCatalogById(@RequestParam("id") Integer categoryId) {
        return FruitShopResponse.success(categoryService.getCurrentCatalogById(categoryId));
    }

}
