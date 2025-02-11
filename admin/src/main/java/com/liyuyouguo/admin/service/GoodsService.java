package com.liyuyouguo.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.beans.dto.shop.*;
import com.liyuyouguo.common.beans.vo.*;
import com.liyuyouguo.common.commons.FruitGreetError;
import com.liyuyouguo.common.commons.FruitGreetException;
import com.liyuyouguo.common.entity.shop.*;
import com.liyuyouguo.common.mapper.*;
import com.liyuyouguo.common.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsService {

    private final GoodsMapper goodsMapper;

    private final CategoryMapper categoryMapper;

    private final ProductMapper productMapper;

    private final GoodsSpecificationMapper goodsSpecificationMapper;

    private final FreightTemplateMapper freightTemplateMapper;

    private final GoodsGalleryMapper goodsGalleryMapper;

    private final CartMapper cartMapper;

    private final SpecificationMapper specificationMapper;

    public PageResult<GoodsVo> getGoodsWithCategoryAndProduct(int page, int size, String name) {
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Goods::getName, "%" + name + "%")
                .eq(Goods::getIsDelete, 0)
                .orderByAsc(Goods::getSortOrder);
        Page<Goods> goodsPage = goodsMapper.selectPage(new Page<>(page, size), queryWrapper);
        return this.getGoodsVoPage(goodsPage);
    }

    private PageResult<GoodsVo> getGoodsVoPage(Page<Goods> goodsPage) {
        List<GoodsVo> goodsList = new ArrayList<>();
        if (goodsPage.getRecords().isEmpty()) {
            return new PageResult<>();
        }
        Collection<GoodsVo> goodsPageData = ConvertUtils.convertCollection(goodsPage.getRecords(), GoodsVo::new,
                (s, t) -> {
                    t.setIsOnSale(s.getIsOnSale() == 1);
                    t.setIsIndex(s.getIsIndex() == 1);
                }).orElseThrow();
        for (GoodsVo goods : goodsPageData) {
            Category category = categoryMapper.selectById(goods.getCategoryId());
            goods.setCategoryName(category != null ? category.getName() : "");

            List<Product> products = productMapper.selectList(new LambdaQueryWrapper<Product>()
                    .eq(Product::getGoodsId, goods.getId())
                    .eq(Product::getIsDelete, 0));
            List<ProductVo> productVos = (List<ProductVo>) ConvertUtils.convertCollection(products, ProductVo::new,
                    (s, t) -> t.setIsOnSale(s.getIsOnSale().toString())).orElseThrow();
            for (ProductVo product : productVos) {
                GoodsSpecification spec = goodsSpecificationMapper.selectById(product.getGoodsSpecificationIds());
                product.setValue(spec != null ? spec.getValue() : "");
            }
            goods.setProduct(productVos);
            goodsList.add(goods);
        }
        return ConvertUtils.convert(goodsPage, PageResult<GoodsVo>::new, (s, t) -> t.setRecords(goodsList)).orElseThrow();
    }

    public ExpressDataVo getExpressData() {
        List<ExpressOptionVo> kd = new ArrayList<>();
        List<ExpressOptionVo> cate = new ArrayList<>();

        // 获取快递模板数据
        List<FreightTemplate> kdData = freightTemplateMapper.selectList(Wrappers.lambdaQuery(FreightTemplate.class)
                .eq(FreightTemplate::getIsDelete, 0));
        for (FreightTemplate item : kdData) {
            ExpressOptionVo expressOptionVo = new ExpressOptionVo();
            expressOptionVo.setLabel(item.getName());
            expressOptionVo.setValue(item.getId());
            kd.add(expressOptionVo);
        }

        // 获取分类数据
        List<Category> cateData = categoryMapper.selectList(Wrappers.lambdaQuery(Category.class)
                .eq(Category::getParentId, 0));
        for (Category item : cateData) {
            ExpressOptionVo expressOptionVo = new ExpressOptionVo();
            expressOptionVo.setLabel(item.getName());
            expressOptionVo.setValue(item.getId());
            cate.add(expressOptionVo);
        }
        ExpressDataVo expressDataVo = new ExpressDataVo();
        expressDataVo.setKd(kd);
        expressDataVo.setCate(cate);
        return expressDataVo;
    }

    public Integer copyGoods(Integer goodsId) {
        // 查询原商品数据
        Goods originalGoods = goodsMapper.selectById(goodsId);
        if (originalGoods == null) {
            throw new FruitGreetException(FruitGreetError.GOODS_EMPTY);
        }
        // 删除原商品的 ID，设置 is_on_sale 为 0
        // 清空原商品的 ID
        originalGoods.setId(null);
        originalGoods.setIsOnSale(0);

        // 新增商品数据
        goodsMapper.insert(originalGoods);
        // 获取插入后的商品 ID
        Integer insertId = originalGoods.getId();

        // 查询原商品的商品画廊数据
        List<GoodsGallery> goodsGalleryList = goodsGalleryMapper.selectList(
                Wrappers.lambdaQuery(GoodsGallery.class)
                        .eq(GoodsGallery::getGoodsId, goodsId)
                        .eq(GoodsGallery::getIsDelete, 0)
        );

        for (GoodsGallery item : goodsGalleryList) {
            GoodsGallery newGallery = new GoodsGallery();
            newGallery.setImgUrl(item.getImgUrl());
            newGallery.setSortOrder(item.getSortOrder());
            newGallery.setGoodsId(insertId);
            goodsGalleryMapper.insert(newGallery);
        }

        return insertId;
    }

    public void updateStock(String goodsSn, Integer goodsNumber) {
        // 执行更新操作
        productMapper.update(Wrappers.lambdaUpdate(Product.class)
                .eq(Product::getGoodsSn, goodsSn)
                .set(Product::getGoodsNumber, goodsNumber));
    }

    public void updateGoodsNumber() {
        // 查询所有未删除且在售的商品
        List<Goods> allGoods = goodsMapper.selectList(Wrappers.lambdaQuery(Goods.class)
                .eq(Goods::getIsDelete, 0)
                .eq(Goods::getIsOnSale, 1));

        // 遍历每个商品
        for (Goods item : allGoods) {
            // 查询该商品下所有产品的库存总和
            Integer goodsSum = productMapper.selectList(Wrappers.lambdaQuery(Product.class)
                            .eq(Product::getGoodsId, item.getId()))
                    .stream()
                    .mapToInt(Product::getGoodsNumber)
                    .sum();

            // 更新商品的库存数量
            goodsMapper.update(Wrappers.lambdaUpdate(Goods.class)
                    .eq(Goods::getId, item.getId())
                    .set(Goods::getGoodsNumber, goodsSum));
        }
    }

    public PageResult<GoodsVo> getOnSaleGoods(int page, int size) {
        // 查询上架的商品
        Page<Goods> goodsPage = goodsMapper.selectPage(
                new Page<>(page, size),
                Wrappers.lambdaQuery(Goods.class)
                        .eq(Goods::getIsDelete, 0)
                        .eq(Goods::getIsOnSale, 1)
                        .orderByAsc(Goods::getSortOrder)
        );
        return this.getGoodsVoPage(goodsPage);
    }

    public PageResult<GoodsVo> out(int page, int size) {
        Page<Goods> goodsPage = goodsMapper.selectPage(new Page<>(page, size),
                Wrappers.lambdaQuery(Goods.class)
                        .eq(Goods::getIsDelete, 0)
                        .le(Goods::getGoodsNumber, 0)
                        .orderByAsc(Goods::getSortOrder)
        );
        return this.getGoodsVoPage(goodsPage);
    }

    public PageResult<GoodsVo> drop(int page, int size) {
        Page<Goods> goodsPage = goodsMapper.selectPage(
                new Page<>(page, size),
                Wrappers.lambdaQuery(Goods.class)
                        .eq(Goods::getIsDelete, 0)
                        .eq(Goods::getIsOnSale, 0)
                        .orderByDesc(Goods::getId)
        );
        return this.getGoodsVoPage(goodsPage);
    }

    public PageResult<GoodsVo> sort(int page, int size, int index) {
        LambdaQueryWrapper<Goods> queryWrapper = Wrappers.lambdaQuery(Goods.class).eq(Goods::getIsDelete, 0);
        if (index == 1) {
            queryWrapper.orderByDesc(Goods::getSellVolume);
        } else if (index == 2) {
            queryWrapper.orderByDesc(Goods::getRetailPrice);
        } else if (index == 3) {
            queryWrapper.orderByDesc(Goods::getGoodsNumber);
        }
        Page<Goods> goodsPage = goodsMapper.selectPage(
                new Page<>(page, size), queryWrapper);
        return this.getGoodsVoPage(goodsPage);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateSaleStatus(Integer id, String status) {
        // 根据status设置is_on_sale
        int sale = "true".equals(status) ? 1 : 0;

        // 更新商品的销售状态
        goodsMapper.update(Wrappers.lambdaUpdate(Goods.class)
                .eq(Goods::getId, id)
                .set(Goods::getIsOnSale, sale)
        );

        // 更新购物车中商品的销售状态
        cartMapper.update(Wrappers.lambdaUpdate(Cart.class)
                .eq(Cart::getGoodsId, id)
                .set(Cart::getIsOnSale, sale)
                .set(Cart::getChecked, sale)
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProductStatus(Integer id, Integer status) {
        // 更新产品的销售状态
        productMapper.update(Wrappers.lambdaUpdate(Product.class)
                .eq(Product::getId, id)
                .set(Product::getIsOnSale, status)
        );

        // 更新购物车中与该产品相关的销售状态
        cartMapper.update(Wrappers.lambdaUpdate(Cart.class)
                .eq(Cart::getProductId, id)
                .eq(Cart::getIsDelete, 0)
                .set(Cart::getIsOnSale, status)
        );
    }

    public void updateIndexShowStatus(Integer id, String status) {
        // 将status转换为0或1
        int stat = "true".equals(status) ? 1 : 0;

        // 更新商品的首页显示状态
        goodsMapper.update(Wrappers.lambdaUpdate(Goods.class)
                .eq(Goods::getId, id)
                .set(Goods::getIsIndex, stat)
        );
    }

    public GoodsInfoVo getGoodsInfo(Integer id) {
        // 获取商品信息
        Goods goods = goodsMapper.selectOne(
                Wrappers.lambdaQuery(Goods.class)
                        .eq(Goods::getId, id)
        );

        // 创建返回的数据对象
        GoodsInfoVo infoData = new GoodsInfoVo();
        infoData.setInfo(goods);
        if (goods != null) {
            infoData.setCategoryId(goods.getCategoryId());
        }
        return infoData;
    }

    public List<SpecificationOptionVo> getAllSpecifications() {
        // 查询所有规格信息
        List<Specification> specInfo = specificationMapper.selectList(
                Wrappers.lambdaQuery(Specification.class)
                        .gt(Specification::getId, 0)
        );

        // 将查询结果转换为specOptionsData格式
        List<SpecificationOptionVo> specOptionsData = new ArrayList<>();
        for (Specification item : specInfo) {
            SpecificationOptionVo info = new SpecificationOptionVo();
            info.setValue(item.getId());
            info.setLabel(item.getName());
            specOptionsData.add(info);
        }
        return specOptionsData;
    }

    public List<CategoryOptionVo> getAllCategory() {
        // 查询一级分类
        List<Category> categoryL1 = categoryMapper.selectList(
                Wrappers.lambdaQuery(Category.class)
                        .eq(Category::getIsShow, 1)
                        .eq(Category::getLevel, "L1")
        );

        // 查询二级分类
        List<Category> categoryL2 = categoryMapper.selectList(
                Wrappers.lambdaQuery(Category.class)
                        .eq(Category::getIsShow, 1)
                        .eq(Category::getLevel, "L2")
        );

        // 将数据合并成符合要求的格式
        List<CategoryOptionVo> newData = new ArrayList<>();
        for (Category item : categoryL1) {
            List<CategoryOptionVo> children = new ArrayList<>();
            for (Category cItem : categoryL2) {
                if (cItem.getParentId().equals(item.getId())) {
                    CategoryOptionVo child = new CategoryOptionVo();
                    child.setValue(cItem.getId());
                    child.setLabel(cItem.getName());
                    children.add(child);
                }
            }
            CategoryOptionVo categoryOption = new CategoryOptionVo();
            categoryOption.setValue(item.getId());
            categoryOption.setLabel(item.getName());
            categoryOption.setChildren(children);
            newData.add(categoryOption);
        }

        return newData;
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer store(GoodsStoreDto dto) {
        Goods newGoods = new Goods();
        GoodsDto goods = dto.getInfo();
        Integer goodsId = goods.getId();
        Integer categoryId = dto.getCateId();
        newGoods.setCategoryId(categoryId);
        newGoods.setIsIndex(goods.getIsIndex() ? 1 : 0);
        newGoods.setIsNew(goods.getIsNew() ? 1 : 0);

        if (goodsId > 0) {
            goodsMapper.updateById(newGoods);
            cartMapper.update(Wrappers.lambdaUpdate(Cart.class)
                    .set(Cart::getChecked, goods.getIsOnSale() ? 1 : 0)
                    .set(Cart::getIsOnSale, goods.getIsOnSale() ? 1 : 0)
                    .set(Cart::getListPicUrl, goods.getListPicUrl())
                    .set(Cart::getFreightTemplateId, goods.getFreightTemplateId())
                    .eq(Cart::getGoodsId, goodsId));
            productMapper.update(Wrappers.lambdaUpdate(Product.class)
                    .set(Product::getIsDelete, 1)
                    .eq(Product::getGoodsId, goodsId));
            goodsSpecificationMapper.update(Wrappers.lambdaUpdate(GoodsSpecification.class)
                    .set(GoodsSpecification::getIsDelete, 1)
                    .eq(GoodsSpecification::getGoodsId, goodsId));

            updateProductAndSpecification(dto);

            updateGallery(dto);

        } else {
            newGoods.setId(null);
            goodsMapper.insert(newGoods);
            goodsId = newGoods.getId();
            addSpecificationsAndProducts(dto, goodsId);
            addGallery(dto, goodsId);
        }

        updateGoodsPrices(goodsId);

        return goodsId;
    }

    private void updateProductAndSpecification(GoodsStoreDto dto) {
        List<GoodsSpecificationDto> specData = dto.getSpecData();
        Integer specValue = dto.getSpecValue();
        Integer goodsId = dto.getInfo().getId();

        for (GoodsSpecificationDto spec : specData) {
            if (spec.getId() > 0) {

                cartMapper.update(Wrappers.lambdaUpdate(Cart.class)
                        .set(Cart::getRetailPrice, spec.getRetailPrice())
                        .set(Cart::getGoodsSpecifitionNameValue, spec.getValue())
                        .set(Cart::getGoodsSn, spec.getGoodsSn())
                        .eq(Cart::getProductId, spec.getId())
                        .eq(Cart::getIsDelete, 0));
                Product product = ConvertUtils.convert(spec, Product::new, (s, t) -> t.setIsDelete(0)).orElseThrow();
                productMapper.updateById(product);

                goodsSpecificationMapper.update(Wrappers.lambdaUpdate(GoodsSpecification.class)
                        .set(GoodsSpecification::getIsDelete, 0)
                        .set(GoodsSpecification::getValue, spec.getValue())
                        .set(GoodsSpecification::getSpecificationId, specValue)
                        .eq(GoodsSpecification::getId, spec.getId()));
            } else {
                GoodsSpecification specification = new GoodsSpecification();
                specification.setValue(spec.getValue());
                specification.setGoodsId(goodsId);
                specification.setSpecificationId(specValue);
                goodsSpecificationMapper.insert(specification);

                Product product = new Product();
                product.setGoodsId(goodsId);
                product.setGoodsSpecificationIds(specification.getId().toString());
                productMapper.insert(product);
            }
        }
    }

    private void updateGallery(GoodsStoreDto dto) {
        List<GalleryDto> galleryList = dto.getInfo().getGallery();
        for (int i = 0; i < galleryList.size(); i++) {
            GalleryDto gallery = galleryList.get(i);
            if (gallery.getIsDelete() == 1 && gallery.getId() > 0) {
                goodsGalleryMapper.update(Wrappers.lambdaUpdate(GoodsGallery.class)
                        .set(GoodsGallery::getIsDelete, 1)
                        .eq(GoodsGallery::getId, gallery.getId()));
            } else if (gallery.getIsDelete() == 0 && gallery.getId() > 0) {
                goodsGalleryMapper.update(Wrappers.lambdaUpdate(GoodsGallery.class)
                        .set(GoodsGallery::getSortOrder, i)
                        .eq(GoodsGallery::getId, gallery.getId()));
            } else if (gallery.getIsDelete() == 0 && gallery.getId() == 0) {
                GoodsGallery newGallery = new GoodsGallery();
                newGallery.setGoodsId(dto.getInfo().getId());
                newGallery.setSortOrder(i);
                newGallery.setImgUrl(gallery.getUrl());
                goodsGalleryMapper.insert(newGallery);
            }
        }
    }

    private void addSpecificationsAndProducts(GoodsStoreDto dto, Integer goodsId) {
        for (GoodsSpecificationDto spec : dto.getSpecData()) {
            GoodsSpecification specification = new GoodsSpecification();
            specification.setValue(spec.getValue());
            specification.setGoodsId(goodsId);
            specification.setSpecificationId(dto.getSpecValue());
            goodsSpecificationMapper.insert(specification);

            Product product = new Product();
            product.setGoodsId(goodsId);
            product.setGoodsSpecificationIds(specification.getId().toString());
            product.setIsOnSale(1);
            productMapper.insert(product);
        }
    }

    private void addGallery(GoodsStoreDto dto, Integer goodsId) {
        List<GalleryDto> galleryList = dto.getInfo().getGallery();
        for (int i = 0; i < galleryList.size(); i++) {
            GalleryDto galleryDto = galleryList.get(i);
            GoodsGallery gallery = new GoodsGallery();
            gallery.setGoodsId(goodsId);
            gallery.setSortOrder(i);
            gallery.setImgUrl(galleryDto.getUrl());
            goodsGalleryMapper.insert(gallery);
        }
    }

    private void updateGoodsPrices(Integer goodsId) {
        List<Product> products = productMapper.selectList(
                Wrappers.lambdaQuery(Product.class)
                        .eq(Product::getGoodsId, goodsId)
                        .eq(Product::getIsOnSale, 1)
                        .eq(Product::getIsDelete, 0));

        if (products.size() > 1) {
            int goodsNum = products.stream().mapToInt(Product::getGoodsNumber).sum();
            double maxPrice = products.stream().mapToDouble(p -> p.getRetailPrice().doubleValue()).max().orElse(0);
            double minPrice = products.stream().mapToDouble(p -> p.getRetailPrice().doubleValue()).min().orElse(0);
            double maxCost = products.stream().mapToDouble(p -> p.getCost().doubleValue()).max().orElse(0);
            double minCost = products.stream().mapToDouble(p -> p.getCost().doubleValue()).min().orElse(0);

            String goodsPrice = minPrice == maxPrice ? String.valueOf(minPrice) : minPrice + "~" + maxPrice;
            String costPrice = minCost + "~" + maxCost;

            goodsMapper.update(Wrappers.lambdaUpdate(Goods.class)
                    .set(Goods::getGoodsNumber, goodsNum)
                    .set(Goods::getRetailPrice, goodsPrice)
                    .set(Goods::getCostPrice, costPrice)
                    .set(Goods::getMinRetailPrice, minPrice)
                    .set(Goods::getMinCostPrice, minCost)
                    .eq(Goods::getId, goodsId));
        } else {
            Product product = products.get(0);
            goodsMapper.update(Wrappers.lambdaUpdate(Goods.class)
                    .set(Goods::getGoodsNumber, product.getGoodsNumber())
                    .set(Goods::getRetailPrice, product.getRetailPrice())
                    .set(Goods::getCostPrice, product.getCost())
                    .set(Goods::getMinRetailPrice, product.getRetailPrice())
                    .set(Goods::getMinCostPrice, product.getCost())
                    .eq(Goods::getId, goodsId));
        }
    }

    public void updatePrice(GoodsUpdatePriceDto dto) {
        Integer goodsId = dto.getGoodsId();

        // 更新规格信息
        goodsSpecificationMapper.update(Wrappers.lambdaUpdate(GoodsSpecification.class)
                .set(GoodsSpecification::getValue, dto.getValue())
                .eq(GoodsSpecification::getId, dto.getGoodsSpecificationIds())
        );

        // 更新商品信息
        Product product = ConvertUtils.convert(dto, Product::new).orElseThrow();
        productMapper.updateById(product);

        // 查询商品规格是否至少有一个
        List<Product> productList = productMapper.selectList(Wrappers.lambdaQuery(Product.class)
                .eq(Product::getGoodsId, goodsId)
                .eq(Product::getIsOnSale, 1)
                .eq(Product::getIsDelete, 0)
        );
        if (productList.isEmpty()) {
            throw new FruitGreetException(FruitGreetError.GOODS_LEAST_ONE);
        }

        // 更新购物车相关数据
        cartMapper.update(Wrappers.lambdaUpdate(Cart.class)
                .set(Cart::getRetailPrice, dto.getRetailPrice())
                .set(Cart::getGoodsSpecifitionNameValue, dto.getValue())
                .set(Cart::getGoodsSn, dto.getGoodsSn())
                .eq(Cart::getProductId, dto.getId())
                .eq(Cart::getIsDelete, 0)
        );

        dto.setValue(null);

        // 更新商品库存和价格
        this.updateGoodsPrices(goodsId);
    }

    public void checkSku(CheckSkuDto dto) {
        Product existingProduct;
        if (dto.getId() > 0) {
            existingProduct = productMapper.selectOne(Wrappers.lambdaQuery(Product.class)
                    .ne(Product::getId, dto.getId())
                    .eq(Product::getGoodsSn, dto.getGoodsSn())
                    .eq(Product::getIsDelete, 0)
                    .last("LIMIT 1"));
        } else {
            existingProduct = productMapper.selectOne(Wrappers.lambdaQuery(Product.class)
                    .eq(Product::getGoodsSn, dto.getGoodsSn())
                    .eq(Product::getIsDelete, 0)
                    .last("LIMIT 1"));
        }
        if (existingProduct != null) {
            throw new FruitGreetException(FruitGreetError.SKU_REPEAT);
        }
    }

    public Integer updateSort(CategoryUpdateSortDto dto) {
        return goodsMapper.update(Wrappers.lambdaUpdate(Goods.class)
                        .set(Goods::getSortOrder, dto.getSort())
                        .eq(Goods::getId, dto.getId()));
    }

//    public Integer updateShortName(GoodsUpdateShortNameDto dto) {
//        return goodsMapper.update(Wrappers.lambdaUpdate(Goods.class)
//                .eq(Goods::getId, dto.getId())
//                .set(Goods::getShortName, dto.getShortName())
//        );
//    }

    public List<GoodsGallery> galleryList(Integer goodsId) {
        return goodsGalleryMapper.selectList(
                Wrappers.lambdaQuery(GoodsGallery.class)
                        .eq(GoodsGallery::getGoodsId, goodsId)
                        .eq(GoodsGallery::getIsDelete, 0)
        );
    }

    public void addGallery(GoodsGalleryDto dto) {
        GoodsGallery gallery = new GoodsGallery();
        gallery.setGoodsId(dto.getGoodsId());
        gallery.setImgUrl(dto.getUrl());
        goodsGalleryMapper.insert(gallery);
    }

    public GalleryListVo getGalleryList(Integer goodsId) {
        List<GoodsGallery> galleryList = goodsGalleryMapper.selectList(
                Wrappers.lambdaQuery(GoodsGallery.class)
                        .eq(GoodsGallery::getGoodsId, goodsId)
                        .eq(GoodsGallery::getIsDelete, 0)
                        .orderByAsc(GoodsGallery::getSortOrder)
        );

        List<GalleryVo> galleryData = galleryList.stream().map(item -> {
            GalleryVo vo = new GalleryVo();
            vo.setId(item.getId());
            vo.setUrl(item.getImgUrl());
            vo.setIsDelete(0);
            return vo;
        }).toList();
        GalleryListVo galleryListVo = new GalleryListVo();
        galleryListVo.setGalleryData(galleryData);
        return galleryListVo;
    }

    public void deleteGalleryFile(GoodsGalleryDeleteDto dto) {
        goodsGalleryMapper.update(Wrappers.lambdaUpdate(GoodsGallery.class)
                .eq(GoodsGallery::getId, dto.getId())
                .set(GoodsGallery::getIsDelete, 1));
    }

    @Transactional(rollbackFor = Exception.class)
    public void galleryEdit(GoodsGalleryEditDto dto) {
        for (GoodsGalleryEditDto.GoodsGalleryEditItem item : dto.getData()) {
            goodsGalleryMapper.update(Wrappers.lambdaUpdate(GoodsGallery.class)
                    .eq(GoodsGallery::getId, item.getId())
                    .set(GoodsGallery::getSortOrder, item.getSortOrder()));
        }
    }

    public void deleteListPicUrl(Integer id) {
        goodsMapper.update(Wrappers.lambdaUpdate(Goods.class).eq(Goods::getId, id).set(Goods::getListPicUrl, 0));
    }

    @Transactional(rollbackFor = Exception.class)
    public void destroy(Integer id) {
        // 更新商品表的删除状态
        goodsMapper.update(Wrappers.lambdaUpdate(Goods.class)
                .eq(Goods::getId, id)
                .set(Goods::getIsDelete, 1));

        // 更新产品表的删除状态
        productMapper.update(Wrappers.lambdaUpdate(Product.class)
                .eq(Product::getGoodsId, id)
                .set(Product::getIsDelete, 1));

        // 更新商品规格表的删除状态
        goodsSpecificationMapper.update(Wrappers.lambdaUpdate(GoodsSpecification.class)
                .eq(GoodsSpecification::getGoodsId, id)
                .set(GoodsSpecification::getIsDelete, 1));
    }

    public String uploadHttpsImage(GoodsUploadImageDto dto) {
//        String accessKey = "yourAccessKey";  // Replace with your actual access key
//        String secretKey = "yourSecretKey";  // Replace with your actual secret key
//        String domain = "yourDomain";  // Replace with your actual domain
//
//        Auth auth = Auth.create(accessKey, secretKey);
//        Configuration cfg = new Configuration(Zone.zone0);  // Set the appropriate zone
//        BucketManager bucketManager = new BucketManager(auth, cfg);
//
//        String bucket = "yourBucket";  // Replace with your actual bucket name
//        String key = UUID.randomUUID().toString().replaceAll("-", "");  // Generate unique key
//        String url = dto.getUrl();
//
//        try {
//            bucketManager.fetch(url, bucket, key);  // Fetch the image from the provided URL
//            return domain + key;  // Return the full image URL
//        } catch (QiniuException e) {
//            e.printStackTrace();
//            return null;
//        }
        return null;
    }

}
