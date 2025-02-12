package com.liyuyouguo.admin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liyuyouguo.common.beans.dto.shop.CategorySaveDto;
import com.liyuyouguo.common.beans.dto.shop.CategoryUpdateSortDto;
import com.liyuyouguo.common.beans.vo.CategoryVo;
import com.liyuyouguo.common.entity.shop.Category;
import com.liyuyouguo.common.mapper.CategoryMapper;
import com.liyuyouguo.common.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<CategoryVo> index() {
        // 查询所有分类，并按 sort_order 升序排列
        List<Category> data = categoryMapper.selectList(Wrappers.lambdaQuery(Category.class)
                .orderByAsc(Category::getSortOrder));

        // 过滤出顶级分类（parent_id = 0）
        List<CategoryVo> topCategory = data.stream()
                .filter(item -> item.getParentId() == 0)
                .map(d -> ConvertUtils.convert(d, CategoryVo::new).orElseThrow())
                .toList();

        List<CategoryVo> resultList = new ArrayList<>();
        topCategory.forEach(item -> {
            item.setLevel("1");
            resultList.add(item);
            data.stream()
                    .filter(child -> child.getParentId().equals(item.getId()))
                    .forEach(d -> resultList.add(ConvertUtils.convert(d, CategoryVo::new, (s, t) -> t.setLevel("2")).orElseThrow()));
        });
        return resultList;
    }

    public int updateSort(CategoryUpdateSortDto dto) {
        return categoryMapper.update(Wrappers.lambdaUpdate(Category.class)
                .set(Category::getSortOrder, dto.getSort())
                .eq(Category::getId, dto.getId()));
    }

    public List<Category> getTopCategories() {
        return categoryMapper.selectList(Wrappers.lambdaQuery(Category.class)
                .eq(Category::getParentId, 0)
                .orderByAsc(Category::getId));
    }

    public Category getCategoryById(Long id) {
        return categoryMapper.selectOne(Wrappers.lambdaQuery(Category.class)
                .eq(Category::getId, id)
                .last("LIMIT 1"));
    }

    public Category store(CategorySaveDto categorySaveDto) {
        Category category = new Category();
        category.setName(categorySaveDto.getName());
        category.setParentId(categorySaveDto.getParentId());
        category.setSortOrder(categorySaveDto.getSortOrder());
        category.setIsShow(categorySaveDto.getIsShow());
        category.setIsChannel(categorySaveDto.getIsChannel());
        category.setIsCategory(categorySaveDto.getIsCategory());

        if (categorySaveDto.getId() != null && categorySaveDto.getId() > 0) {
            category.setId(categorySaveDto.getId());
            categoryMapper.updateById(category);
        } else {
            categoryMapper.insert(category);
        }

        return category;
    }

    public boolean deleteCategory(Integer id) {
        // 检查是否存在子分类
        long count = categoryMapper.selectCount(Wrappers.lambdaQuery(Category.class)
                .eq(Category::getParentId, id));

        if (count > 0) {
            return false;
        }

        // 删除该分类
        categoryMapper.delete(Wrappers.lambdaQuery(Category.class)
                .eq(Category::getId, id)
                .last("LIMIT 1"));

        return true;
    }

    public void updateShowStatus(Integer id, Boolean status) {
        int isShow = Boolean.TRUE.equals(status) ? 1 : 0;

        categoryMapper.update(Wrappers.lambdaUpdate(Category.class)
                .set(Category::getIsShow, isShow)
                .eq(Category::getId, id));
    }

    public void updateChannelStatus(Integer id, Boolean status) {
        int isChannel = Boolean.TRUE.equals(status) ? 1 : 0;

        categoryMapper.update(Wrappers.lambdaUpdate(Category.class)
                .set(Category::getIsChannel, isChannel)
                .eq(Category::getId, id));
    }

    public void updateCategoryStatus(Integer id, Boolean status) {
        int isCategory = Boolean.TRUE.equals(status) ? 1 : 0;

        categoryMapper.update(Wrappers.lambdaUpdate(Category.class)
                .set(Category::getIsCategory, isCategory)
                .eq(Category::getId, id));
    }

    public void deleteBannerImage(Integer id) {
        categoryMapper.update(Wrappers.lambdaUpdate(Category.class)
                .set(Category::getImgUrl, "")
                .eq(Category::getId, id));
    }

    public void deleteIconImage(Integer id) {
        categoryMapper.update(Wrappers.lambdaUpdate(Category.class)
                .set(Category::getIconUrl, "")
                .eq(Category::getId, id));
    }
}
