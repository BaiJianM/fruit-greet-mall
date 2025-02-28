package com.liyuyouguo.admin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liyuyouguo.common.beans.dto.shop.ProductUpdateDto;
import com.liyuyouguo.common.beans.dto.shop.SpecificationAddDto;
import com.liyuyouguo.common.beans.dto.shop.UpdateSpecificationDto;
import com.liyuyouguo.common.beans.vo.GoodsSpecVo;
import com.liyuyouguo.common.commons.FruitGreetError;
import com.liyuyouguo.common.commons.FruitGreetException;
import com.liyuyouguo.common.entity.shop.*;
import com.liyuyouguo.common.mapper.*;
import com.liyuyouguo.common.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SpecificationService {

    private final SpecificationMapper specificationMapper;

    private final GoodsSpecificationMapper goodsSpecificationMapper;

    private final ProductMapper productMapper;

    private final CartMapper cartMapper;

    private final GoodsMapper goodsMapper;

    public List<Specification> getAllSpecifications() {
        return specificationMapper.selectList(Wrappers.lambdaQuery(Specification.class).gt(Specification::getId, 0));
    }

    public GoodsSpecVo getGoodsSpec(Integer id) {
        List<Product> productList = productMapper.selectList(
                Wrappers.lambdaQuery(Product.class)
                        .eq(Product::getGoodsId, id)
                        .eq(Product::getIsDelete, 0)
        );
        //TODO 这里只有一层，以后如果有多重型号，如一件商品既有颜色又有尺寸时，这里的代码是不对的。以后再写。
        AtomicReference<Integer> specificationId = new AtomicReference<>(0);

        List<GoodsSpecVo.GoodsSpecDataVo> goodsSpecData =
                (List<GoodsSpecVo.GoodsSpecDataVo>) ConvertUtils.convertCollection(productList, GoodsSpecVo.GoodsSpecDataVo::new, (s, t) -> {
                    GoodsSpecification specValue = goodsSpecificationMapper.selectOne(
                            Wrappers.lambdaQuery(GoodsSpecification.class)
                                    .eq(GoodsSpecification::getId, s.getGoodsSpecificationIds())
                                    .eq(GoodsSpecification::getIsDelete, 0)
                    );

                    if (specValue != null) {
                        t.setValue(specValue.getValue());
                        specificationId.set(specValue.getSpecificationId());
                    }
                }).orElseThrow();

        return new GoodsSpecVo(goodsSpecData, specificationId.get());
    }

    @Transactional(rollbackFor = Exception.class)
    public void productUpdate(ProductUpdateDto dto) {
        // 更新购物车中对应商品的价格
        cartMapper.update(Wrappers.lambdaUpdate(Cart.class)
                .set(Cart::getRetailPrice, dto.getRetailPrice())
                .eq(Cart::getGoodsSn, dto.getGoodsSn())
        );

        // 更新 product 表的商品信息
        productMapper.update(
                Wrappers.lambdaUpdate(Product.class)
                        .set(Product::getGoodsNumber, dto.getGoodsNumber())
                        .set(Product::getGoodsWeight, dto.getGoodsWeight())
                        .set(Product::getCost, dto.getCost())
                        .set(Product::getRetailPrice, dto.getRetailPrice())
                        .eq(Product::getGoodsSn, dto.getGoodsSn())
        );

        // 获取商品规格 ID 和商品 ID
        Product product = productMapper.selectOne(
                Wrappers.lambdaQuery(Product.class)
                        .select(Product::getGoodsSpecificationIds, Product::getGoodsId)
                        .eq(Product::getGoodsSn, dto.getGoodsSn())
                        .last("LIMIT 1")
        );

        // 更新商品规格的值
        goodsSpecificationMapper.update(
                Wrappers.lambdaUpdate(GoodsSpecification.class)
                        .set(GoodsSpecification::getValue, dto.getValue())
                        .eq(GoodsSpecification::getId, product.getGoodsSpecificationIds())
        );

        // 计算商品价格区间
        // todo 价格显示为区间
        List<Product> productList = productMapper.selectList(
                Wrappers.lambdaQuery(Product.class)
                        .eq(Product::getGoodsId, product.getGoodsId())
        );

        if (productList.size() > 1) {
            List<Product> products = productMapper.selectList(
                    Wrappers.lambdaQuery(Product.class).eq(Product::getGoodsId, product.getGoodsId())
            );

            int goodsNum = products.stream().mapToInt(Product::getGoodsNumber).sum();
            double maxPrice = products.stream().mapToDouble(p -> p.getRetailPrice().doubleValue()).max().orElse(0);
            double minPrice = products.stream().mapToDouble(p -> p.getRetailPrice().doubleValue()).min().orElse(0);
            double maxCost = products.stream().mapToDouble(p -> p.getCost().doubleValue()).max().orElse(0);
            double minCost = products.stream().mapToDouble(p -> p.getCost().doubleValue()).min().orElse(0);

            goodsMapper.update(
                    Wrappers.lambdaUpdate(Goods.class)
                            .set(Goods::getGoodsNumber, goodsNum)
                            .set(Goods::getRetailPrice, minPrice + "-" + maxPrice)
                            .set(Goods::getCostPrice, minCost + "-" + maxCost)
                            .set(Goods::getMinRetailPrice, minPrice)
                            .set(Goods::getMinCostPrice, minCost)
                            .eq(Goods::getId, product.getGoodsId())
            );
        } else {
            goodsMapper.update(
                    Wrappers.lambdaUpdate(Goods.class)
                            .set(Goods::getGoodsNumber, dto.getGoodsNumber())
                            .set(Goods::getRetailPrice, dto.getRetailPrice())
                            .set(Goods::getCostPrice, dto.getCost())
                            .set(Goods::getMinRetailPrice, dto.getRetailPrice())
                            .set(Goods::getMinCostPrice, dto.getCost())
                            .eq(Goods::getId, product.getGoodsId())
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void productDele(Integer id) {
        // 查询商品规格 ID 和商品 ID
        Product product = productMapper.selectOne(
                Wrappers.lambdaQuery(Product.class)
                        .select(Product::getGoodsSpecificationIds, Product::getGoodsId)
                        .eq(Product::getId, id)
        );

        // 删除商品记录
        productMapper.deleteById(id);

        // 删除商品规格记录
        goodsSpecificationMapper.deleteById(product.getGoodsSpecificationIds());

        // 检查商品是否还有剩余规格
        List<Product> remainingProducts = productMapper.selectList(
                Wrappers.lambdaQuery(Product.class)
                        .eq(Product::getGoodsId, product.getGoodsId())
        );

        if (!remainingProducts.isEmpty()) {
            List<Product> products = productMapper.selectList(
                    Wrappers.lambdaQuery(Product.class).eq(Product::getGoodsId, product.getGoodsId())
            );

            int goodsNum = products.stream().mapToInt(Product::getGoodsNumber).sum();
            double minPrice = products.stream().mapToDouble(p -> p.getRetailPrice().doubleValue()).min().orElse(0);

            // 更新商品库存和最低零售价
            goodsMapper.update(
                    Wrappers.lambdaUpdate(Goods.class)
                            .set(Goods::getGoodsNumber, goodsNum)
                            .set(Goods::getRetailPrice, minPrice)
                            .eq(Goods::getId, product.getGoodsId())
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delePrimarySpec(Integer id) {
        // 删除商品相关规格
        productMapper.delete(
                Wrappers.lambdaQuery(Product.class)
                        .eq(Product::getGoodsId, id)
        );

        // 删除商品规格记录
        goodsSpecificationMapper.delete(
                Wrappers.lambdaQuery(GoodsSpecification.class)
                        .eq(GoodsSpecification::getGoodsId, id)
        );

        // 更新商品库存和零售价
        goodsMapper.update(
                Wrappers.lambdaUpdate(Goods.class)
                        .set(Goods::getGoodsNumber, 0)
                        .set(Goods::getRetailPrice, BigDecimal.ZERO)
                        .eq(Goods::getId, id)
        );

    }

    public Specification getDetail(Integer id) {
        return specificationMapper.selectById(id);
    }

    public Integer addSpecification(SpecificationAddDto dto) {
        Specification specification = new Specification();
        specification.setName(dto.getName());
        specification.setSortOrder(dto.getSortOrder());
        specificationMapper.insert(specification);
        return specification.getId();
    }

    public void update(UpdateSpecificationDto dto) {
        Specification specification = ConvertUtils.convert(dto, Specification::new).orElseThrow();
        specificationMapper.updateById(specification);
    }

    public void delete(Integer id) {
        List<GoodsSpecification> goodsSpecList =
                goodsSpecificationMapper.selectList(Wrappers.lambdaQuery(GoodsSpecification.class)
                        .eq(GoodsSpecification::getSpecificationId, id));

        if (!goodsSpecList.isEmpty()) {
            throw new FruitGreetException(FruitGreetError.SKU_GOODS_EXIST);
        }

        specificationMapper.deleteById(id);
    }

}
