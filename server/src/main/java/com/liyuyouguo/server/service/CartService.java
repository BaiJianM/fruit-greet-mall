package com.liyuyouguo.server.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liyuyouguo.common.beans.dto.shop.CartAddDto;
import com.liyuyouguo.common.beans.dto.shop.CartCheckedDto;
import com.liyuyouguo.common.beans.dto.shop.CartDeleteDto;
import com.liyuyouguo.common.beans.dto.shop.CartUpdateDto;
import com.liyuyouguo.common.beans.vo.*;
import com.liyuyouguo.common.entity.shop.*;
import com.liyuyouguo.common.mapper.*;
import com.liyuyouguo.common.commons.FruitGreetException;
import com.liyuyouguo.common.commons.FruitGreetError;
import com.liyuyouguo.common.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * 购物车服务类
 *
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper cartMapper;

    private final ProductMapper productMapper;

    private final GoodsMapper goodsMapper;

    private final OrderGoodsMapper orderGoodsMapper;

    private final AddressMapper addressMapper;

    private final GoodsSpecificationMapper goodsSpecificationMapper;

    private final FreightTemplateMapper freightTemplateMapper;

    private final FreightTemplateDetailMapper freightTemplateDetailMapper;

    private final FreightTemplateGroupMapper freightTemplateGroupMapper;

    private final AddressService addressService;

    /**
     * 获取购物车商品的总件件数
     *
     * @return CartVo 购物车数量
     */
    public CartCountVo getGoodsCount() {
        CartCountVo cartCountVo = new CartCountVo();
        CartInfoVo cartInfo = this.getCart(0);
        if (cartInfo == null) {
            CartTotalVo cartTotalVo = new CartTotalVo();
            cartTotalVo.setGoodsCount(0);
            cartCountVo.setCartTotal(cartTotalVo);
            return cartCountVo;
        }
        Cart cart = cartMapper.selectOne(Wrappers.lambdaQuery(Cart.class)
                .eq(Cart::getUserId, cartInfo.getCartTotal().getUserId())
                .eq(Cart::getIsDelete, 0)
                .eq(Cart::getIsFast, 1));
        cart.setIsDelete(1);
        cartMapper.updateById(cart);
        cartCountVo.setCartTotal(cartInfo.getCartTotal());
        return cartCountVo;
    }

    public CartInfoVo getCart(Integer isFast) {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        List<Cart> carts = cartMapper.selectList(Wrappers.lambdaQuery(Cart.class)
                .eq(Cart::getUserId, userId)
                .eq(Cart::getIsDelete, 0)
                .eq(Cart::getIsFast, isFast));
        if (carts.isEmpty()) {
            return null;
        }
        int goodsCount = 0;
        BigDecimal goodsAmount = new BigDecimal(0);
        int checkedGoodsCount = 0;
        BigDecimal checkedGoodsAmount = new BigDecimal(0);
        int numberChange = 0;
        List<CartVo> cartList = new ArrayList<>();
        for (Cart cart : carts) {
            CartVo cartVo = ConvertUtils.convert(cart, CartVo::new).orElseThrow();
            Product product = productMapper.selectById(cart.getProductId());
            if (product == null) {
                cart.setIsDelete(1);
                cartMapper.updateById(cart);
            } else {
                BigDecimal retailPrice = product.getRetailPrice();
                Integer productNum = product.getGoodsNumber();
                if (productNum <= 0 || product.getIsOnSale() == 0) {
                    cart.setChecked(0);
                    cartMapper.updateById(cart);
                    cartVo.setNumber(0);
                } else if (productNum > 0 && productNum < cart.getNumber()) {
                    cartVo.setNumber(productNum);
                    numberChange = 1;
                } else if (productNum > 0 && cart.getNumber() == 0) {
                    cartVo.setNumber(1);
                    numberChange = 1;
                }
                goodsCount += cart.getNumber();
                goodsAmount = goodsAmount.add(retailPrice.multiply(new BigDecimal(cart.getNumber())));
                cartVo.setRetailPrice(retailPrice);
                // TODO 这个if有待验证
                if (cart.getChecked() == 1 && productNum > 0) {
                    checkedGoodsCount += cart.getNumber();
                    checkedGoodsAmount = checkedGoodsAmount.add(retailPrice.multiply(new BigDecimal(cart.getNumber())));
                }
                // 查找商品的图片
                Goods goods = goodsMapper.selectById(cart.getGoodsId());
                cartVo.setListPicUrl(goods.getListPicUrl());
                cartVo.setWeightCount(cartVo.getNumber() * cartVo.getGoodsWeight());

                cart.setNumber(cartVo.getNumber());
                cart.setAddPrice(retailPrice);
                cartMapper.updateById(cart);
            }
            cartList.add(cartVo);
        }
        CartInfoVo cartInfoVo = new CartInfoVo();
        cartInfoVo.setCartList(cartList);

        CartTotalVo cartTotalVo = new CartTotalVo();
        cartTotalVo.setGoodsCount(goodsCount);
        cartTotalVo.setGoodsAmount(goodsAmount.setScale(2, RoundingMode.DOWN));
        cartTotalVo.setCheckedGoodsCount(checkedGoodsCount);
        cartTotalVo.setCheckedGoodsAmount(checkedGoodsAmount.setScale(2, RoundingMode.DOWN));
        cartTotalVo.setUserId(userId);
        cartTotalVo.setNumberChange(numberChange);

        cartInfoVo.setCartTotal(cartTotalVo);
        return cartInfoVo;
    }

    /**
     * 获取购物车信息，所有对购物车的增删改操作，都要重新返回购物车的信息
     *
     * @return CartInfoVo 购物车信息
     */
    public CartInfoVo getIndex() {
        return this.getCart(0);
    }

    /**
     * 添加商品到购物车
     *
     * @param dto 传参
     * @return CartInfoVo 当前购物车信息
     */
    public CartInfoVo add(CartAddDto dto) {
        Integer goodsId = dto.getGoodsId();
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        Integer productId = dto.getProductId();
        Integer number = dto.getNumber();
        Integer addType = dto.getAddType();
        LocalDateTime currentTime = LocalDateTime.now();

        // 判断商品是否可以购买
        Goods goodsInfo = goodsMapper.selectById(goodsId);
        if (goodsInfo == null || goodsInfo.getIsOnSale() == 0) {
            throw new FruitGreetException(FruitGreetError.ITEM_NOT_AVAILABLE);
        }

        // 取得规格的信息，判断规格库存
        Product productInfo = productMapper.selectById(productId);
        if (productInfo == null || productInfo.getGoodsNumber() < number) {
            throw new FruitGreetException(FruitGreetError.INSUFFICIENT_STOCK);
        }

        // 查询购物车中是否已有该规格商品
        Cart cartInfo = cartMapper.selectOne(Wrappers.lambdaQuery(Cart.class)
                .eq(Cart::getUserId, userId)
                .eq(Cart::getProductId, productId)
                .eq(Cart::getIsDelete, 0));

        BigDecimal retailPrice = productInfo.getRetailPrice();

        if (addType == 1) {
            // 快速添加模式
            cartMapper.update(Wrappers.lambdaUpdate(Cart.class)
                    .eq(Cart::getUserId, userId)
                    .eq(Cart::getIsDelete, 0)
                    .set(Cart::getChecked, 0));

            // 添加规格名和值（若存在）
            List<String> goodsSpecifitionValues = new ArrayList<>();
            if (productInfo.getGoodsSpecificationIds() != null) {
                goodsSpecifitionValues = this.getGoodsSpecifitionValues(productInfo.getGoodsId(), productInfo.getGoodsSpecificationIds());
            }

            Cart cartData = new Cart();
            cartData.setGoodsId(productInfo.getGoodsId());
            cartData.setProductId(productId);
            cartData.setGoodsSn(productInfo.getGoodsSn());
            cartData.setGoodsName(goodsInfo.getName());
            cartData.setGoodsAka(productInfo.getGoodsName());
            cartData.setGoodsWeight(productInfo.getGoodsWeight());
            cartData.setFreightTemplateId(goodsInfo.getFreightTemplateId());
            cartData.setListPicUrl(goodsInfo.getListPicUrl());
            cartData.setNumber(number);
            cartData.setUserId(userId);
            cartData.setRetailPrice(retailPrice);
            cartData.setAddPrice(retailPrice);
            cartData.setGoodsSpecifitionNameValue(String.join(";", goodsSpecifitionValues));
            cartData.setGoodsSpecifitionIds(productInfo.getGoodsSpecificationIds());
            cartData.setChecked(1);
            cartData.setAddTime(currentTime);
            cartData.setIsFast(1);

            cartMapper.insert(cartData);
            return this.getCart(1);
        } else {
            // 普通添加模式
            if (cartInfo == null) {
                // 添加新商品
                List<String> goodsSpecifitionValues = new ArrayList<>();
                if (productInfo.getGoodsSpecificationIds() != null) {
                    goodsSpecifitionValues = this.getGoodsSpecifitionValues(productInfo.getGoodsId(), productInfo.getGoodsSpecificationIds());
                }

                Cart cartData = new Cart();
                cartData.setGoodsId(productInfo.getGoodsId());
                cartData.setProductId(productId);
                cartData.setGoodsSn(productInfo.getGoodsSn());
                cartData.setGoodsName(goodsInfo.getName());
                cartData.setGoodsAka(productInfo.getGoodsName());
                cartData.setGoodsWeight(productInfo.getGoodsWeight());
                cartData.setFreightTemplateId(goodsInfo.getFreightTemplateId());
                cartData.setListPicUrl(goodsInfo.getListPicUrl());
                cartData.setNumber(number);
                cartData.setUserId(userId);
                cartData.setRetailPrice(retailPrice);
                cartData.setAddPrice(retailPrice);
                cartData.setGoodsSpecifitionNameValue(String.join(";", goodsSpecifitionValues));
                cartData.setGoodsSpecifitionIds(productInfo.getGoodsSpecificationIds());
                cartData.setChecked(1);
                cartData.setAddTime(currentTime);

                cartMapper.insert(cartData);
            } else {
                // 如果购物车中已经存在该商品，则更新数量
                if (productInfo.getGoodsNumber() < (number + cartInfo.getNumber())) {
                    throw new FruitGreetException(FruitGreetError.INSUFFICIENT_STOCK);
                }
                cartInfo.setRetailPrice(retailPrice);
                cartInfo.setNumber(cartInfo.getNumber() + number);
                cartMapper.updateById(cartInfo);
            }
            return this.getCart(0);
        }
    }

    private List<String> getGoodsSpecifitionValues(Integer goodsId, String goodsSpecificationIdsStr) {
        List<Integer> goodsSpecificationIds =
                Stream.of(goodsSpecificationIdsStr.split(",")).map(Integer::parseInt).toList();
        return goodsSpecificationMapper.selectList(Wrappers.lambdaQuery(GoodsSpecification.class)
                        .eq(GoodsSpecification::getGoodsId, goodsId)
                        .eq(GoodsSpecification::getIsDelete, 0)
                        .in(GoodsSpecification::getId, goodsSpecificationIds))
                .stream().map(GoodsSpecification::getValue).toList();
    }

    /**
     * 更新指定的购物车信息
     *
     * @param dto 更新传参
     * @return CartInfoVo 当前购物车信息
     */
    public CartInfoVo update(CartUpdateDto dto) {
        Integer productId = dto.getProductId();
        Integer number = dto.getNumber();
        // 获取新的商品规格信息，判断库存
        Product productInfo = productMapper.selectOne(Wrappers.lambdaQuery(Product.class)
                .eq(Product::getId, productId)
                .eq(Product::getIsDelete, 0));
        if (productInfo == null || productInfo.getGoodsNumber() < number) {
            throw new FruitGreetException(FruitGreetError.INSUFFICIENT_STOCK);
        }
        // 获取购物车中对应商品信息
        Cart cartInfo = cartMapper.selectOne(Wrappers.lambdaQuery(Cart.class)
                .eq(Cart::getId, dto.getCartId())
                .eq(Cart::getIsDelete, 0));
        if (cartInfo == null) {
            throw new FruitGreetException(FruitGreetError.CART_ITEM_NOT_EXIST);
        }
        // 如果是同一个商品规格，只更新数量
        if (cartInfo.getProductId().equals(productId)) {
            cartInfo.setNumber(number);
            cartMapper.updateById(cartInfo);
            return this.getCart(0);
        }
        throw new FruitGreetException(FruitGreetError.INVALID_CART_OPERATION);
    }

    /**
     * 是否选择商品，如果已经选择，则取消选择，批量操作
     *
     * @param dto 传参
     * @return CartInfoVo 当前购物车信息
     */
    public CartInfoVo checked(CartCheckedDto dto) {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        String productIds = dto.getProductIds();
        // 将商品ID字符串分割为列表
        List<Integer> productIdList = Arrays.stream(productIds.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();
        // 更新选中状态
        cartMapper.update(Wrappers.lambdaUpdate(Cart.class)
                .set(Cart::getChecked, dto.getIsChecked() ? 1 : 0)
                .eq(Cart::getUserId, userId)
                .eq(Cart::getIsDelete, 0)
                .in(Cart::getProductId, productIdList));
        // 返回更新后的购物车信息
        return this.getCart(0);
    }

    /**
     * 删除选中的购物车商品
     *
     * @param dto 传参
     * @return CartInfoVo 当前购物车信息
     */
    public CartInfoVo delete(CartDeleteDto dto) {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        // 更新购物车中指定商品的删除状态
        cartMapper.update(Wrappers.lambdaUpdate(Cart.class)
                .set(Cart::getIsDelete, 1)
                .in(Cart::getProductId, dto.getProductIds())
                .eq(Cart::getUserId, userId)
                .eq(Cart::getIsDelete, 0));
        // 返回更新后的购物车信息
        return this.getCart(0);
    }

    /**
     * 订单提交前的检验和填写相关订单信息
     *
     * @param orderId   订单id
     * @param type      是否团购
     * @param addressId 收货地址id
     * @param addType   添加商品的类型
     * @return CartCheckoutVo 订单提交前的检验和填写相关订单信息
     */
    public CartCheckoutVo checkout(Integer orderId, Integer type, Integer addressId, Integer addType) {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        // 购物车的数量
        int goodsCount = 0;
        // 购物车的总价
        BigDecimal goodsMoney = BigDecimal.ZERO;
        BigDecimal freightPrice = BigDecimal.ZERO;
        int outStock = 0;
        CartInfoVo cartData = new CartInfoVo();
        // 获取要购买的商品
        if (type == 0) {
            if (addType == 0) {
                cartData = this.getCart(0);
            } else if (addType == 1) {
                cartData = this.getCart(1);
            } else if (addType == 2) {
                cartData = this.getAgainCart(orderId);
            }
        }
        List<CartVo> checkedGoodsList = cartData.getCartList().stream().filter(v -> v.getChecked() == 1).toList();
        for (CartVo cartVo : checkedGoodsList) {
            goodsCount += cartVo.getNumber();
            goodsMoney = goodsMoney.add(BigDecimal.valueOf(cartVo.getNumber()).multiply(cartVo.getRetailPrice()));
            if (cartVo.getGoodsNumber() <= 0 || cartVo.getIsOnSale() == 0) {
                outStock++;
            }
        }
        if (addType == 2) {
            List<OrderGoods> againGoods = orderGoodsMapper.selectList(Wrappers.lambdaQuery(OrderGoods.class)
                    .eq(OrderGoods::getOrderId, orderId));
            int againGoodsCount = againGoods.stream().mapToInt(OrderGoods::getNumber).sum();
            if (goodsCount != againGoodsCount) {
                outStock = 1;
            }
        }
        // 选择的收货地址
        Address checkedAddress;
        if (addressId == null || addressId == 0) {
            checkedAddress = addressMapper.selectOne(Wrappers.lambdaQuery(Address.class)
                    .eq(Address::getIsDefault, 1)
                    .eq(Address::getUserId, userId)
                    .eq(Address::getIsDelete, 0));
        } else {
            checkedAddress = addressMapper.selectOne(Wrappers.lambdaQuery(Address.class)
                    .eq(Address::getId, addressId)
                    .eq(Address::getUserId, userId)
                    .eq(Address::getIsDelete, 0));
        }

        if (checkedAddress != null) {
            // 运费开始
            // 先将促销规则中符合满件包邮或者满金额包邮的规则找到；
            // 先看看是不是属于偏远地区。
            Integer provinceId = checkedAddress.getProvinceId();
            List<FreightTemplate> freightTempArray = freightTemplateMapper.selectList(Wrappers.lambdaQuery(FreightTemplate.class)
                    .eq(FreightTemplate::getIsDelete, 0));
            List<FreightDataVo> freightData = freightTempArray.stream().map(item -> {
                FreightDataVo data = new FreightDataVo();
                data.setId(item.getId());
                data.setNumber(0);
                data.setMoney(BigDecimal.ZERO);
                data.setGoodsWeight(0d);
                data.setFreightType(item.getFreightType());
                return data;
            }).toList();
            // 按件计算和按重量计算的区别是：按件，只要算goods_number就可以了，按重量要goods_number*goods_weight
            for (FreightDataVo dataVo : freightData) {
                for (CartVo cartItem : checkedGoodsList) {
                    // 这个在判断，购物车中的商品是否属于这个运费模版，如果是，则加一，但是，这里要先判断下，这个商品是否符合满件包邮或满金额包邮，如果是包邮的，那么要去掉
                    if (dataVo.getId().equals(cartItem.getFreightTemplateId())) {
                        dataVo.setNumber(dataVo.getNumber() + cartItem.getNumber());
                        dataVo.setMoney(dataVo.getMoney().add(BigDecimal.valueOf(cartItem.getNumber()).multiply(cartItem.getRetailPrice())));
                        dataVo.setGoodsWeight(dataVo.getGoodsWeight() + cartItem.getNumber() * cartItem.getGoodsWeight());
                    }
                }
            }
            addressService.setAddressInfo(ConvertUtils.convert(checkedAddress, AddressVo::new).orElseThrow());
            for (FreightDataVo item : freightData) {
                if (item.getNumber() == 0) {
                    continue;
                }
                FreightTemplateDetail ex = freightTemplateDetailMapper.selectOne(Wrappers.lambdaQuery(FreightTemplateDetail.class)
                        .eq(FreightTemplateDetail::getTemplateId, item.getId())
                        .eq(FreightTemplateDetail::getArea, provinceId)
                        .eq(FreightTemplateDetail::getIsDelete, 0));
                BigDecimal freightPriceForItem = BigDecimal.ZERO;
                // 不为空，说明有模板，那么应用模板，先去判断是否符合指定的包邮条件，不满足，那么根据type 是按件还是按重量
                if (ex != null) {
                    FreightTemplateGroup groupData = freightTemplateGroupMapper.selectById(ex.getGroupId());
                    if (groupData != null && groupData.getIsDelete() == 0) {
                        // 4种情况，1、free_by_number > 0  2,free_by_money > 0  3,free_by_number free_by_money > 0,4都等于0
                        freightPriceForItem = this.checkAndGetFreightPrice(item, groupData);
                    } else {
                        log.error("运费模板组为空");
                    }
                } else {
                    FreightTemplateGroup groupData = freightTemplateGroupMapper.selectOne(Wrappers.lambdaQuery(FreightTemplateGroup.class)
                            .eq(FreightTemplateGroup::getTemplateId, item.getId())
                            .eq(FreightTemplateGroup::getArea, 0)
                            .eq(FreightTemplateGroup::getIsDelete, 0));
                    if (groupData != null) {
                        freightPriceForItem = this.checkAndGetFreightPrice(item, groupData);
                    } else {
                        log.error("运费模板组为空[1]");
                    }
                }
                freightPrice = freightPrice.max(freightPriceForItem);
            }
        }

        // 计算订单的费用，商品总价
        BigDecimal goodsTotalPrice = cartData.getCartTotal().getCheckedGoodsAmount();
        // 订单的总价
        BigDecimal orderTotalPrice = goodsTotalPrice.add(freightPrice).setScale(2, RoundingMode.DOWN);
        // 减去其它支付的金额后，要实际支付的金额 TODO 这里有问题，待测
        BigDecimal actualPrice = orderTotalPrice.setScale(2, RoundingMode.DOWN);

        CartCheckoutVo cartCheckoutVo = new CartCheckoutVo();
        cartCheckoutVo.setCheckedAddress(checkedAddress);
        cartCheckoutVo.setFreightPrice(freightPrice);
        cartCheckoutVo.setCheckedGoodsList(checkedGoodsList);
        cartCheckoutVo.setGoodsTotalPrice(goodsTotalPrice);
        cartCheckoutVo.setOrderTotalPrice(orderTotalPrice);
        cartCheckoutVo.setActualPrice(actualPrice);
        cartCheckoutVo.setGoodsCount(goodsCount);
        cartCheckoutVo.setOutStock(outStock);
        cartCheckoutVo.setNumberChange(cartData.getCartTotal().getNumberChange());

        return cartCheckoutVo;
    }

    private BigDecimal checkAndGetFreightPrice(FreightDataVo item, FreightTemplateGroup groupData) {
        BigDecimal freightPriceForItem = BigDecimal.ZERO;
        FreightTemplate freightTemplate = freightTemplateMapper.selectById(item.getId());
        if (freightTemplate != null) {
            if (freightTemplate.getFreightType() == 0) {
                if (item.getNumber() > groupData.getStart()) {
                    // todo 如果续件是2怎么办？？？
                    // 说明大于首件了
                    freightPriceForItem = new BigDecimal(groupData.getStart()).multiply(groupData.getStartFee())
                            .add(new BigDecimal(item.getNumber() - 1).multiply(groupData.getAddFee()));
                } else {
                    freightPriceForItem = new BigDecimal(groupData.getStart()).multiply(groupData.getStartFee());
                }
            } else if (freightTemplate.getFreightType() == 1) {
                // todo 如果续件是2怎么办？？？
                // 说明大于首件了
                if (item.getGoodsWeight() > groupData.getStart()) {
                    freightPriceForItem = new BigDecimal(groupData.getStart()).multiply(groupData.getStartFee())
                            .add(BigDecimal.valueOf(item.getGoodsWeight() - 1).multiply(groupData.getAddFee()));
                } else {
                    freightPriceForItem = new BigDecimal(groupData.getStart()).multiply(groupData.getStartFee());
                }
            }
            if (groupData.getFreeByNumber() > 0 && (item.getNumber() >= groupData.getFreeByNumber())) {
                freightPriceForItem = BigDecimal.ZERO;
            }
            if (groupData.getFreeByMoney().compareTo(BigDecimal.ZERO) > 0 && (item.getMoney().compareTo(groupData.getFreeByMoney()) >= 0)) {
                freightPriceForItem = BigDecimal.ZERO;
            }
        } else {
            log.error("运费模板为空");
        }
        return freightPriceForItem;
    }

    private CartInfoVo getAgainCart(Integer orderFrom) {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        // 查询订单商品
        List<OrderGoods> againGoods = orderGoodsMapper.selectList(Wrappers.lambdaQuery(OrderGoods.class)
                .eq(OrderGoods::getOrderId, orderFrom));
        // 更新购物车中商品为未选中状态
        cartMapper.update(Wrappers.lambdaUpdate(Cart.class)
                .set(Cart::getChecked, 0)
                .eq(Cart::getUserId, userId)
                .eq(Cart::getIsDelete, 0));

        // 将订单中的商品重新添加到购物车
        for (OrderGoods orderGood : againGoods) {
            this.addAgain(orderGood.getGoodsId(), orderGood.getProductId(), orderGood.getNumber());
        }

        // 查询购物车中的商品
        List<Cart> cartList = cartMapper.selectList(Wrappers.lambdaQuery(Cart.class)
                .eq(Cart::getUserId, userId)
                .eq(Cart::getIsFast, 0)
                .eq(Cart::getIsDelete, 0));

        int goodsCount = 0;
        BigDecimal goodsAmount = BigDecimal.ZERO;
        int checkedGoodsCount = 0;
        BigDecimal checkedGoodsAmount = BigDecimal.ZERO;

        List<CartVo> cartVoList = (List<CartVo>) ConvertUtils.convertCollection(cartList, CartVo::new).orElseThrow();

        // 统计购物车信息
        for (CartVo cartVo : cartVoList) {
            goodsCount += cartVo.getNumber();
            goodsAmount = goodsAmount.add(BigDecimal.valueOf(cartVo.getNumber()).multiply(cartVo.getRetailPrice()));

            if (cartVo.getChecked() != null && cartVo.getChecked() == 1) {
                checkedGoodsCount += cartVo.getNumber();
                checkedGoodsAmount = checkedGoodsAmount.add(BigDecimal.valueOf(cartVo.getNumber()).multiply(cartVo.getRetailPrice()));
            }

            // 查找商品的图片
            Goods goodsInfo = goodsMapper.selectById(cartVo.getGoodsId());
            if (goodsInfo.getGoodsNumber() <= 0) {
                cartMapper.update(Wrappers.lambdaUpdate(Cart.class)
                        .set(Cart::getChecked, 0)
                        .eq(Cart::getProductId, cartVo.getProductId())
                        .eq(Cart::getUserId, userId)
                        .eq(Cart::getChecked, 1)
                        .eq(Cart::getIsDelete, 0));
            }
            cartVo.setListPicUrl(goodsInfo.getListPicUrl());
            cartVo.setGoodsNumber(goodsInfo.getGoodsNumber());
            cartVo.setWeightCount(cartVo.getNumber() * cartVo.getGoodsWeight());
        }

        CartTotalVo cartTotalVo = new CartTotalVo();
        cartTotalVo.setGoodsCount(goodsCount);
        cartTotalVo.setGoodsAmount(goodsAmount.setScale(2, RoundingMode.DOWN));
        cartTotalVo.setCheckedGoodsCount(checkedGoodsCount);
        cartTotalVo.setCheckedGoodsAmount(checkedGoodsAmount.setScale(2, RoundingMode.DOWN));
        cartTotalVo.setUserId(userId);

        CartInfoVo cartInfoVo = new CartInfoVo();
        cartInfoVo.setCartTotal(cartTotalVo);
        cartInfoVo.setCartList(cartVoList);

        return cartInfoVo;
    }

    private void addAgain(Integer goodsId, Integer productId, Integer number) {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        LocalDateTime currentTime = LocalDateTime.now();

        // 查询商品信息，判断是否下架
        Goods goodsInfo = goodsMapper.selectById(goodsId);
        if (goodsInfo == null || goodsInfo.getIsOnSale() == 0) {
            throw new FruitGreetException(FruitGreetError.ITEM_NOT_AVAILABLE);
        }
        // 取得规格的信息,判断规格库存
        Product productInfo = productMapper.selectById(productId);
        if (productInfo == null || productInfo.getGoodsNumber() < number) {
            throw new FruitGreetException(FruitGreetError.INSUFFICIENT_STOCK);
        }
        // 查询购物车中是否存在此规格商品
        Cart cartInfo = cartMapper.selectOne(Wrappers.lambdaQuery(Cart.class)
                .eq(Cart::getUserId, userId)
                .eq(Cart::getProductId, productId)
                .eq(Cart::getIsDelete, 0));

        BigDecimal retailPrice = productInfo.getRetailPrice();

        if (cartInfo == null) {
            // 添加操作
            // 添加规格名和值
            // 购物车中不存在该商品，添加新商品
            List<String> goodsSpecifitionValue = new ArrayList<>();
            if (productInfo.getGoodsSpecificationIds() != null) {
                goodsSpecifitionValue = this.getGoodsSpecifitionValues(productInfo.getGoodsId(), productInfo.getGoodsSpecificationIds());
            }
            Cart cartData = new Cart();
            cartData.setGoodsId(productInfo.getGoodsId());
            cartData.setProductId(productId);
            cartData.setGoodsSn(productInfo.getGoodsSn());
            cartData.setGoodsName(goodsInfo.getName());
            cartData.setGoodsAka(productInfo.getGoodsName());
            cartData.setGoodsWeight(productInfo.getGoodsWeight());
            cartData.setFreightTemplateId(goodsInfo.getFreightTemplateId());
            cartData.setListPicUrl(goodsInfo.getListPicUrl());
            cartData.setNumber(number);
            cartData.setUserId(userId);
            cartData.setRetailPrice(retailPrice);
            cartData.setAddPrice(retailPrice);
            cartData.setGoodsSpecifitionNameValue(String.join(";", goodsSpecifitionValue));
            cartData.setGoodsSpecifitionIds(productInfo.getGoodsSpecificationIds());
            cartData.setChecked(1);
            cartData.setAddTime(currentTime);

            cartMapper.insert(cartData);
        } else {
            // 购物车中已存在该商品，更新数量
            if (productInfo.getGoodsNumber() < (number + cartInfo.getNumber())) {
                throw new FruitGreetException(FruitGreetError.INSUFFICIENT_STOCK);
            }
            cartInfo.setRetailPrice(retailPrice);
            cartInfo.setChecked(1);
            cartInfo.setNumber(number);
            cartMapper.updateById(cartInfo);
        }
    }

}
