package com.liyuyouguo.server.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.beans.dto.shop.OrderQueryDto;
import com.liyuyouguo.common.beans.dto.shop.OrderSubmitDto;
import com.liyuyouguo.common.beans.dto.shop.OrderUpdateDto;
import com.liyuyouguo.common.beans.vo.*;
import com.liyuyouguo.common.entity.shop.*;
import com.liyuyouguo.common.mapper.*;
import com.liyuyouguo.common.commons.FruitGreetException;
import com.liyuyouguo.common.commons.FruitGreetError;
import com.liyuyouguo.common.config.FruitGreetProperties;
import com.liyuyouguo.common.utils.ConvertUtils;
import com.liyuyouguo.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 订单服务类
 *
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final FruitGreetProperties properties;

    private final OrderMapper orderMapper;

    private final OrderGoodsMapper orderGoodsMapper;

    private final AddressService addressService;

    private final CartMapper cartMapper;

    private final GoodsMapper goodsMapper;

    private final ProductMapper productMapper;

    private final SettingsMapper settingsMapper;

    private final OrderExpressMapper orderExpressMapper;

    /**
     * 我的页面获取订单数状态
     *
     * @return OrderCountVo 订单数
     */
    public OrderCountVo getOrderStatusCount() {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        if (userId != 0) {
            OrderCountVo vo = new OrderCountVo();
            Long toPay = orderMapper.selectCount(Wrappers.lambdaQuery(Order.class)
                    .eq(Order::getUserId, userId)
                    .eq(Order::getIsDelete, 0)
                    .lt(Order::getOrderType, 7)
                    .in(Order::getOrderStatus, Arrays.asList(101, 801)));
            vo.setToPay(toPay);
            Long toDelivery = orderMapper.selectCount(Wrappers.lambdaQuery(Order.class)
                    .eq(Order::getUserId, userId)
                    .eq(Order::getIsDelete, 0)
                    .lt(Order::getOrderType, 7)
                    .eq(Order::getOrderStatus, 300));
            vo.setToDelivery(toDelivery);
            Long toReceive = orderMapper.selectCount(Wrappers.lambdaQuery(Order.class)
                    .eq(Order::getUserId, userId)
                    .eq(Order::getIsDelete, 0)
                    .lt(Order::getOrderType, 7)
                    .eq(Order::getOrderStatus, 301));
            vo.setToReceive(toReceive);
            return vo;
        }
        return null;
    }

    /**
     * 获取订单列表
     *
     * @param dto 传参
     * @return PageResult<OrderVo> 订单信息分页
     */
    public PageResult<OrderVo> getOrderList(OrderQueryDto dto) {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        // 根据 showType 获取订单状态列表
        List<Integer> statusList = this.getOrderStatus(dto.getShowType());
        Page<Order> page = new Page<>(dto.getCurrent(), dto.getSize());
        // 查询订单列表
        Page<Order> orderPage = orderMapper.selectPage(page, Wrappers.lambdaQuery(Order.class)
                .eq(Order::getUserId, userId)
                .eq(Order::getIsDelete, 0)
                .lt(Order::getOrderType, 7)
                .in(Order::getOrderStatus, statusList)
                .orderByDesc(Order::getAddTime));
        // 处理订单数据
        List<OrderVo> orderVos = new ArrayList<>();
        for (Order order : orderPage.getRecords()) {
            Integer orderId = order.getId();
            OrderVo vo = new OrderVo();
            // 订单的商品
            List<OrderGoods> orderGoods = orderGoodsMapper.selectList(Wrappers.lambdaQuery(OrderGoods.class)
                    .eq(OrderGoods::getUserId, userId)
                    .eq(OrderGoods::getOrderId, orderId)
                    .eq(OrderGoods::getIsDelete, 0));
            vo.setGoodsList(orderGoods);
            int goodsCount = orderGoods.stream().mapToInt(OrderGoods::getNumber).sum();
            vo.setGoodsCount(goodsCount);
            vo.setAddTime(this.getOrderAddTime(orderId));
            // 订单状态的处理
            vo.setOrderStatusText(this.getOrderStatusText(orderId));
            vo.setHandleOption(this.getOrderHandleOption(orderId));
            orderVos.add(vo);
        }
        return ConvertUtils.convert(orderPage, PageResult<OrderVo>::new, (s, t) -> t.setRecords(orderVos)).orElseThrow();
    }

    public List<Integer> getOrderStatus(Integer showType) {
        List<Integer> status = new ArrayList<>();

        if (showType == null) {
            return Collections.emptyList();
        }

        switch (showType) {
            case 0:
                // 全部订单
                status.addAll(Arrays.asList(101, 102, 103, 201, 202, 203, 300, 301, 302, 303, 401));
                break;
            case 1:
                // 待付款订单
                status.add(101);
                break;
            case 2:
                // 待发货订单
                status.add(300);
                break;
            case 3:
                // 待收货订单
                status.add(301);
                break;
            case 4:
                // 待评价订单
                status.addAll(Arrays.asList(302, 303));
                break;
            default:
                return Collections.emptyList();
        }

        return status;
    }

    private String getOrderAddTime(Integer orderId) {
        return DateUtils.parseTime(orderMapper.selectById(orderId).getAddTime());
    }

    public String getOrderStatusText(Integer orderId) {
        // 根据订单ID查询订单信息
        Order orderInfo = orderMapper.selectById(orderId);
        if (orderInfo == null) {
            throw new FruitGreetException(FruitGreetError.ORDER_NOT_EXIST);
        }
        // 根据订单状态设置状态文本
        return switch (orderInfo.getOrderStatus()) {
            case 101 -> "待付款";
            case 102, 103 -> "交易关闭";
            case 201, 300 -> "待发货";
            case 301 -> "已发货";
            // 到时间，未收货的系统自动收货
            case 401 -> "交易成功";
            // 如果需要，处理未知状态
            default -> "未知状态";
        };
    }

    public OrderHandleOptionVo getOrderHandleOption(Integer orderId) {
        // 初始化操作选项
        OrderHandleOptionVo handleOption = new OrderHandleOptionVo();

        // 查询订单信息
        Order orderInfo = orderMapper.selectById(orderId);
        if (orderInfo == null) {
            throw new FruitGreetException(FruitGreetError.ORDER_NOT_EXIST);
        }

        // 获取订单状态
        Integer orderStatus = orderInfo.getOrderStatus();

        // 根据订单状态设置可操作选项
        switch (orderStatus) {
            // 订单刚创建，可以取消订单，可以继续支付
            case 101, 801:
                handleOption.setCancel(true);
                handleOption.setPay(true);
                break;
            // 如果订单被取消
            case 102, 103, 203, 401:
                handleOption.setDelete(true);
                break;
            // TODO 如果订单已付款，没有发货，则可退款操作（逻辑未实现）
            case 201:
                break;
            // 如果订单申请退款中，没有相关操作
            case 202:
                handleOption.setCancelRefund(true);
                break;
            // 如果订单已经发货，没有收货，则可收货操作
            case 301:
                handleOption.setConfirm(true);
                break;
            default:
                // 其他状态不设置操作选项
                break;
        }

        return handleOption;
    }

    /**
     * 获取订单数量
     *
     * @return Long 订单数量
     */
    public Long getOrderCount(Integer showType) {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        List<Integer> statusList = this.getOrderStatus(showType);
        return orderMapper.selectCount(Wrappers.lambdaQuery(Order.class)
                .eq(Order::getUserId, userId)
                .eq(Order::getIsDelete, 0)
                .in(Order::getOrderStatus, statusList));
    }

    /**
     * 获取订单详情
     *
     * @param orderId 订单id
     * @return OrderDetailVo 订单详情
     */
    public OrderDetailVo getOrderDetail(Integer orderId) {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        // 获取订单信息
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new FruitGreetException(FruitGreetError.ORDER_NOT_EXIST);
        }
        OrderInfoVo orderInfo = ConvertUtils.convert(order, OrderInfoVo::new).orElseThrow();
        addressService.setAddressInfo(orderInfo);

        // 解码订单备注
        orderInfo.setPostscript(new String(Base64.getDecoder().decode(orderInfo.getPostscript())));

        // 获取订单商品信息
        List<OrderGoods> orderGoods = orderGoodsMapper.selectList(Wrappers.lambdaQuery(OrderGoods.class)
                .eq(OrderGoods::getOrderId, orderId)
                .eq(OrderGoods::getUserId, userId)
                .eq(OrderGoods::getIsDelete, 0));

        // 计算商品数量
        int goodsCount = orderGoods.stream().mapToInt(OrderGoods::getNumber).sum();

        // 订单状态文本
        String orderStatusText = this.getOrderStatusText(orderId);
        orderInfo.setOrderStatusText(orderStatusText);

        if (orderInfo.getShippingTime() != null) {
            orderInfo.setConfirmRemainTime(orderInfo.getShippingTime().plusDays(10));
        }

        // 计算支付倒计时
        if (orderInfo.getOrderStatus() == 101 || orderInfo.getOrderStatus() == 801) {
            LocalDateTime finalPayTime = orderInfo.getAddTime().plusHours(2);
            if (LocalDateTime.now().isAfter(finalPayTime)) {
                // 超过支付时间，更新订单状态为取消
                order.setOrderStatus(102);
                orderMapper.updateById(order);
            }
        }
        orderInfo.setOrderStatus(0);
        OrderHandleOptionVo orderHandleOption = this.getOrderHandleOption(orderId);
        OrderTextCodeVo orderTextCode = this.getOrderTextCode(orderId);

        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrderInfo(orderInfo);
        vo.setOrderGoods(orderGoods);
        vo.setHandleOption(orderHandleOption);
        vo.setTextCode(orderTextCode);
        vo.setGoodsCount(goodsCount);
        return vo;
    }

    public OrderTextCodeVo getOrderTextCode(Integer orderId) {
        OrderTextCodeVo textCode = new OrderTextCodeVo();

        // 查询订单信息
        Order orderInfo = orderMapper.selectById(orderId);
        if (orderInfo == null) {
            return textCode;
        }

        // 根据订单状态设置文本状态码
        if (orderInfo.getOrderStatus() == 101) {
            textCode.setPay(true);
            textCode.setCountdown(true);
        }
        if (orderInfo.getOrderStatus() == 102 || orderInfo.getOrderStatus() == 103) {
            textCode.setClose(true);
        }
        // 待发货
        if (orderInfo.getOrderStatus() == 201 || orderInfo.getOrderStatus() == 300) {
            textCode.setDelivery(true);
        }
        // 已发货
        if (orderInfo.getOrderStatus() == 301) {
            textCode.setReceive(true);
        }
        if (orderInfo.getOrderStatus() == 401) {
            textCode.setSuccess(true);
        }

        return textCode;
    }

    /**
     * 获取checkout页面的商品列表
     *
     * @param orderId 订单id
     * @return List<OrderGoodsVo> 商品列表
     */
    public List<OrderGoodsVo> getOrderGoods(Integer orderId) {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        List<OrderGoodsVo> orderGoods;
        if (orderId != null && orderId > 0) {
            List<OrderGoods> orderGoodsList = orderGoodsMapper.selectList(Wrappers.lambdaQuery(OrderGoods.class)
                    .eq(OrderGoods::getUserId, userId)
                    .eq(OrderGoods::getOrderId, orderId)
                    .eq(OrderGoods::getIsDelete, 0));
//            int goodsCount = orderGoods.stream().mapToInt(OrderGoods::getNumber).sum();
            orderGoods = (List<OrderGoodsVo>) ConvertUtils.convert(orderGoodsList, OrderGoodsVo::new).orElseThrow();
        } else {
            List<Cart> cartList = cartMapper.selectList(Wrappers.lambdaQuery(Cart.class)
                    .eq(Cart::getUserId, userId)
                    .eq(Cart::getChecked, 1)
                    .eq(Cart::getIsFast, 0)
                    .eq(Cart::getIsDelete, 0));
            orderGoods = (List<OrderGoodsVo>) ConvertUtils.convert(cartList, OrderGoodsVo::new).orElseThrow();
        }
        return orderGoods;
    }

    /**
     * 取消订单
     *
     * @param orderId 订单id
     * @return Integer 取消订单结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer cancel(Integer orderId) {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        // 检查订单是否可取消
        OrderHandleOptionVo handleOption = this.getOrderHandleOption(orderId);
        if (handleOption.getCancel()) {
            throw new FruitGreetException(FruitGreetError.ORDER_CANNOT_CANCEL);
        }

        // 获取订单商品信息
        List<OrderGoods> orderGoodsList = orderGoodsMapper.selectList(Wrappers.lambdaQuery(OrderGoods.class)
                .eq(OrderGoods::getOrderId, orderId)
                .eq(OrderGoods::getUserId, userId));

        // 还原库存
        for (OrderGoods item : orderGoodsList) {
            Integer goodsId = item.getGoodsId();
            Integer productId = item.getProductId();
            Integer number = item.getNumber();
            Goods goods = goodsMapper.selectById(goodsId);
            goods.setGoodsNumber(goods.getGoodsNumber() + number);
            Product product = productMapper.selectById(productId);
            product.setGoodsNumber(product.getGoodsNumber() + number);
            // 更新商品库存
            goodsMapper.updateById(goods);
            // 更新产品库存
            productMapper.updateById(product);
        }
        // 更新订单状态为已取消
        Order order = orderMapper.selectById(orderId);
        order.setOrderStatus(102);
        return orderMapper.updateById(order);
    }

    /**
     * 删除订单
     *
     * @param orderId 订单id
     * @return Integer 删除订单结果
     */
    public Integer delete(Integer orderId) {
        // 检查订单是否可取消
        OrderHandleOptionVo handleOption = this.getOrderHandleOption(orderId);
        if (handleOption.getDelete()) {
            throw new FruitGreetException(FruitGreetError.ORDER_CANNOT_DELETE);
        }
        return orderMapper.deleteById(orderId);
    }

    /**
     * 确认订单
     *
     * @param orderId 订单id
     * @return Integer 确认订单结果
     */
    public Integer confirm(Integer orderId) {
        // 检查订单是否可确认
        OrderHandleOptionVo handleOption = this.getOrderHandleOption(orderId);
        if (handleOption.getConfirm()) {
            throw new FruitGreetException(FruitGreetError.ORDER_CANNOT_CONFIRM);
        }
        Order order = orderMapper.selectById(orderId);
        order.setOrderStatus(401);
        order.setConfirmTime(LocalDateTime.now());
        return orderMapper.updateById(order);
    }

    /**
     * 完成评论后的订单
     *
     * @param orderId 订单id
     * @return Integer 完成评论结果
     */
    public Integer complete(Integer orderId) {
        // 设置订单已完成
        Order order = orderMapper.selectById(orderId);
        order.setOrderStatus(401);
        order.setConfirmTime(LocalDateTime.now());
        return orderMapper.updateById(order);
    }

    /**
     * 提交订单
     *
     * @param dto 传参
     * @return Order 订单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public Order submit(OrderSubmitDto dto) {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        // 获取收货地址
        Integer addressId = dto.getAddressId();
        BigDecimal freightPrice = dto.getFreightPrice();
        Integer offlinePay = dto.getOfflinePay();
        String postscript = dto.getPostscript();

        Address checkedAddress = addressService.getById(addressId);
        if (checkedAddress == null) {
            throw new FruitGreetException(FruitGreetError.NOT_HAVE_ADDRESS);
        }

        // 获取购物车中选中的商品
        List<Cart> checkedGoodsList = cartMapper.selectList(Wrappers.lambdaQuery(Cart.class)
                .eq(Cart::getUserId, userId)
                .eq(Cart::getChecked, 1)
                .eq(Cart::getIsDelete, 0));
        if (checkedGoodsList.isEmpty()) {
            throw new FruitGreetException(FruitGreetError.NOT_HAVE_GOODS);
        }

        // 检查库存和价格是否变化
        int checkStock = 0;
        int checkPrice = 0;
        for (Cart item : checkedGoodsList) {
            Product product = productMapper.selectById(item.getProductId());
            if (item.getNumber() > product.getGoodsNumber()) {
                checkStock++;
            }
            if (item.getRetailPrice().compareTo(item.getAddPrice()) != 0) {
                checkPrice++;
            }
        }
        if (checkStock > 0) {
            throw new FruitGreetException(FruitGreetError.INSUFFICIENT_STOCK_REORDER);
        }
        if (checkPrice > 0) {
            throw new FruitGreetException(FruitGreetError.PRICE_CHANGED_REORDER);
        }

        // 获取订单使用的红包
        // 如果有用红包，则将红包的数量减少，当减到0时，将该条红包删除
        // 计算商品总价
        double sum = checkedGoodsList.stream()
                .mapToDouble(cartItem -> cartItem.getNumber() * cartItem.getRetailPrice().doubleValue())
                .sum();
        BigDecimal goodsTotalPrice = BigDecimal.valueOf(sum);

        // 订单总价计算
        BigDecimal orderTotalPrice = goodsTotalPrice.add(freightPrice);
        // 减去其它支付的金额后，要实际支付的金额 比如满减等优惠
        BigDecimal actualPrice = orderTotalPrice.subtract(BigDecimal.ZERO);

        // 拼接打印信息
        StringBuilder printInfo = new StringBuilder();
        for (int i = 0; i < checkedGoodsList.size(); i++) {
            Cart cartItem = checkedGoodsList.get(i);
            printInfo.append(i + 1).append("、").append(cartItem.getGoodsAka()).append("【")
                    .append(cartItem.getNumber()).append("】 ");
        }

//        // 获取系统设置
//        Settings def = settingsMapper.selectById(1);
//        String senderName = def.getName();
//        String senderMobile = def.getTel();
//
//        // 获取用户信息
//        User userInfo = userService.getById(userId);

        // 构建订单信息
        Order order = new Order();
        order.setOrderSn(generateOrderNumber());
        order.setUserId(userId);
        order.setConsignee(checkedAddress.getName());
        order.setMobile(checkedAddress.getMobile());
        order.setProvince(checkedAddress.getProvinceId());
        order.setCity(checkedAddress.getCityId());
        order.setDistrict(checkedAddress.getDistrictId());
        order.setAddress(checkedAddress.getAddress());
        // 初始状态为 101
        order.setOrderStatus(101);
        order.setFreightPrice(freightPrice);
        order.setPostscript(postscript);
        order.setAddTime(LocalDateTime.now());
        order.setGoodsPrice(goodsTotalPrice);
        order.setOrderPrice(orderTotalPrice);
        order.setActualPrice(actualPrice);
        order.setChangePrice(actualPrice);
        order.setPrintInfo(printInfo.toString());
        order.setOfflinePay(offlinePay);

        // 保存订单
        int saveOrder = orderMapper.insert(order);
        if (saveOrder != 1) {
            throw new FruitGreetException(FruitGreetError.ORDER_SUBMIT_ERROR);
        }

        // 保存订单商品信息
        List<OrderGoods> orderGoodsData = new ArrayList<>();
        for (Cart goodsItem : checkedGoodsList) {
            OrderGoods orderGoods = new OrderGoods();
            orderGoods.setUserId(userId);
            orderGoods.setOrderId(order.getId());
            orderGoods.setGoodsId(goodsItem.getGoodsId());
            orderGoods.setProductId(goodsItem.getProductId());
            orderGoods.setGoodsName(goodsItem.getGoodsName());
            orderGoods.setGoodsAka(goodsItem.getGoodsAka());
            orderGoods.setListPicUrl(goodsItem.getListPicUrl());
            orderGoods.setRetailPrice(goodsItem.getRetailPrice());
            orderGoods.setNumber(goodsItem.getNumber());
            orderGoods.setGoodsSpecifitionNameValue(goodsItem.getGoodsSpecifitionNameValue());
            orderGoods.setGoodsSpecifitionIds(goodsItem.getGoodsSpecifitionIds());
            orderGoodsData.add(orderGoods);
        }
        orderGoodsData.forEach(orderGoodsMapper::insert);

        // 清空购物车已购买商品
        cartMapper.update(Wrappers.lambdaUpdate(Cart.class)
                .set(Cart::getIsDelete, 1)
                .eq(Cart::getUserId, userId)
                .eq(Cart::getChecked, 1)
                .eq(Cart::getIsDelete, 0));

        return order;

    }

    // 生成订单号
    public static String generateOrderNumber() {
        // 获取当前时间
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDate = dateFormat.format(now);  // 格式化当前时间为年月日时分秒

        // 生成六位随机数
        Random random = new Random();
        int randomNum = 100000 + random.nextInt(900000);  // 生成100000到999999之间的随机数

        return formattedDate + randomNum;  // 组合成订单号
    }

    /**
     * 更新订单
     *
     * @param dto 传参
     * @return Integer 更新订单结果
     */
    public Integer update(OrderUpdateDto dto) {
        Address address = addressService.getById(dto.getAddressId());
        // 更新收货地址和运费
        Order order = orderMapper.selectById(dto.getOrderId());
        order.setConsignee(address.getName());
        order.setMobile(address.getMobile());
        order.setProvince(address.getProvinceId());
        order.setCity(address.getCityId());
        order.setDistrict(address.getDistrictId());
        order.setAddress(address.getAddress());
        // TODO 根据地址计算运费
        return orderMapper.updateById(order);
    }

    /**
     * 查询物流信息asd
     *
     * @param orderId 订单id
     * @return OrderExpress 订单物流信息
     */
    public OrderExpress getExpress(Integer orderId) {
        try {

            // 从数据库查询物流信息
            OrderExpress expressInfo = orderExpressMapper.selectOne(Wrappers.lambdaQuery(OrderExpress.class)
                    .eq(OrderExpress::getOrderId, orderId));
            if (expressInfo == null) {
                throw new FruitGreetException(FruitGreetError.NO_EXPRESS_DATA);
            }

            // 如果is_finish == 1；或者 updateTime 小于 1分钟，
            LocalDateTime updateTime = expressInfo.getUpdateTime();
            if (updateTime == null) {
                throw new FruitGreetException(FruitGreetError.NO_EXPRESS);
            }
            long com = Math.abs(Duration.between(LocalDateTime.now(), updateTime).toMinutes());
            int isFinish = expressInfo.getIsFinish();

            if (isFinish == 1 || com < 20) {
                // 返回物流信息
                return expressInfo;
            } else {
                // 获取快递公司和快递单号
                String shipperCode = expressInfo.getShipperCode();
                String expressNo = expressInfo.getLogisticCode();

                JSONObject lastExpressInfo = this.getExpressInfo(shipperCode, expressNo);

                // 获取快递状态和更新时间
                int deliverystatus = lastExpressInfo.getIntValue("deliverystatus");
                LocalDateTime newUpdateTime = Instant.ofEpochMilli(lastExpressInfo.getLongValue("updateTime"))
                        .atZone(ZoneId.of("Asia/Shanghai"))
                        .toLocalDateTime();

                // 获取状态描述
                String deliveryStatusDesc = getDeliveryStatus(deliverystatus);

                // 是否已签收
                int issign = lastExpressInfo.getIntValue("issign");

                // 物流轨迹
                String traces = lastExpressInfo.get("list").toString();

                // 更新数据库中的物流信息
//                Map<String, Object> dataInfo = Map.of(
//                        "express_status", statusDescription,
//                        "is_finish", issign,
//                        "traces", traces,
//                        "update_time", newUpdateTime
//                );
                expressInfo.setExpressStatus(deliveryStatusDesc);
                expressInfo.setIsFinish(issign);
                expressInfo.setTraces(traces);
                expressInfo.setUpdateTime(newUpdateTime);
                return expressInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FruitGreetException(FruitGreetError.QUERY_EXPRESS_ERROR);
        }
    }

    // 获取快递信息
    public JSONObject getExpressInfo(String shipperCode, String expressNo) throws Exception {
        // 设置请求头
        String url = "http://wuliu.market.alicloudapi.com/kdi?no=" + expressNo + "&type=" + shipperCode;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Authorization", "APPCODE " + properties.getAppCode())
                .GET()
                .build();

        // 发送请求并获取响应
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 解析返回的 JSON 数据
        String responseBody = response.body();
        return JSON.parseObject(responseBody);
    }

    // 根据状态码获取配送状态
    public static String getDeliveryStatus(int status) {
        return switch (status) {
            case 0 -> "快递收件(揽件)";
            case 1 -> "在途中";
            case 2 -> "正在派件";
            case 3 -> "已签收";
            case 4 -> "派送失败(无法联系到收件人或客户要求择日派送，地址不详或手机号不清)";
            case 5 -> "疑难件(收件人拒绝签收，地址有误或不能送达派送区域，收费等原因无法正常派送)";
            case 6 -> "退件签收";
            default -> "未知状态";
        };
    }
}
