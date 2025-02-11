package com.liyuyouguo.admin.controller;

import com.liyuyouguo.admin.service.CategoryService;
import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.dto.shop.CategorySaveDto;
import com.liyuyouguo.common.beans.dto.shop.CategoryUpdateSortDto;
import com.liyuyouguo.common.beans.vo.CategoryVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import com.liyuyouguo.common.entity.shop.Category;
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
@FruitGreetController("/category")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public FruitGreetResponse<List<CategoryVo>> index() {
        return FruitGreetResponse.success(categoryService.index());
    }

    @PostMapping("/updateSort")
    public FruitGreetResponse<Integer> updateSort(@RequestBody CategoryUpdateSortDto dto) {
        return FruitGreetResponse.success(categoryService.updateSort(dto));
    }

    @GetMapping("/topCategory")
    public FruitGreetResponse<List<Category>> topCategory() {
        return FruitGreetResponse.success(categoryService.getTopCategories());
    }

    @GetMapping("/info")
    public FruitGreetResponse<Category> info(@RequestParam Long id) {
        return FruitGreetResponse.success(categoryService.getCategoryById(id));
    }

    @PostMapping("/store")
    public FruitGreetResponse<Category> store(@RequestBody CategorySaveDto categorySaveDto) {
        return FruitGreetResponse.success(categoryService.store(categorySaveDto));
    }

    @PostMapping("/destroy")
    public FruitGreetResponse<Void> destroy(@RequestParam Integer id) {
        boolean success = categoryService.deleteCategory(id);
        return success ? FruitGreetResponse.success() : FruitGreetResponse.fail();
    }

    @GetMapping("/showStatus")
    public FruitGreetResponse<Void> showStatus(@RequestParam Integer id, @RequestParam Boolean status) {
        categoryService.updateShowStatus(id, status);
        return FruitGreetResponse.success();
    }

    @GetMapping("/channelStatus")
    public FruitGreetResponse<Void> channelStatus(@RequestParam Integer id, @RequestParam Boolean status) {
        categoryService.updateChannelStatus(id, status);
        return FruitGreetResponse.success();
    }

    @GetMapping("/categoryStatus")
    public FruitGreetResponse<Void> categoryStatus(@RequestParam Integer id, @RequestParam Boolean status) {
        categoryService.updateCategoryStatus(id, status);
        return FruitGreetResponse.success();
    }

    @PostMapping("/deleteBannerImage")
    public FruitGreetResponse<Void> deleteBannerImage(@RequestParam Integer id) {
        categoryService.deleteBannerImage(id);
        return FruitGreetResponse.success();
    }

    @PostMapping("/deleteIconImage")
    public FruitGreetResponse<Void> deleteIconImage(@RequestParam Integer id) {
        categoryService.deleteIconImage(id);
        return FruitGreetResponse.success();
    }

}
