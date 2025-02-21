package com.liyuyouguo.admin.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.beans.dto.shop.*;
import com.liyuyouguo.common.beans.enums.AliExpressDeliveryStatusEnum;
import com.liyuyouguo.common.beans.vo.*;
import com.liyuyouguo.common.beans.vo.express.AliExpressResponseVo;
import com.liyuyouguo.common.commons.FruitGreetError;
import com.liyuyouguo.common.commons.FruitGreetException;
import com.liyuyouguo.common.entity.shop.*;
import com.liyuyouguo.common.mapper.*;
import com.liyuyouguo.common.service.ExpressService;
import com.liyuyouguo.common.utils.ConvertUtils;
import com.liyuyouguo.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;

    private final OrderExpressMapper orderExpressMapper;

    private final OrderGoodsMapper orderGoodsMapper;

    private final UserMapper userMapper;

    private final RegionMapper regionMapper;

    private final SettingsMapper settingsMapper;

    private final ProductMapper productMapper;

    private final ExpressService expressService;

    private final ShipperMapper shipperMapper;

    public PageResult<OrderAdminVo> getOrderList(OrderQueryAdminDto queryDto) {
        int page = queryDto.getPage();
        int size = queryDto.getSize();

        Page<Order> orderPage;
        if (StringUtils.isBlank(queryDto.getLogisticCode())) {
            // 查询订单数据
            orderPage = orderMapper.selectPage(new Page<>(page, size),
                    Wrappers.lambdaQuery(Order.class)
                            .like(StringUtils.isNotBlank(queryDto.getOrderSn()), Order::getOrderSn, "%" + queryDto.getOrderSn() + "%")
                            .like(StringUtils.isNotBlank(queryDto.getConsignee()), Order::getConsignee, "%" + queryDto.getConsignee() + "%")
                            .in(Order::getOrderStatus, Arrays.asList(queryDto.getStatus().split(",")))
                            .lt(Order::getOrderType, 7)
                            .orderByDesc(Order::getId)
            );
        } else {
            // 根据物流单号查询订单
            OrderExpress orderExpress = orderExpressMapper.selectOne(Wrappers.lambdaQuery(OrderExpress.class)
                    .eq(OrderExpress::getLogisticCode, queryDto.getLogisticCode()));

            if (orderExpress == null) {
                return new PageResult<>();
            }

            orderPage = orderMapper.selectPage(new Page<>(page, size),
                    Wrappers.lambdaQuery(Order.class)
                            .eq(Order::getId, orderExpress.getOrderId())
                            .orderByDesc(Order::getId)
            );
        }

        return convertToOrderVoPage(orderPage);

    }

    private PageResult<OrderAdminVo> convertToOrderVoPage(Page<Order> orderPage) {
        List<OrderAdminVo> orderVoList = (List<OrderAdminVo>) ConvertUtils.convertCollection(orderPage.getRecords(), OrderAdminVo::new, (o, vo) -> {
            // 查询订单商品信息
            List<OrderGoods> orderGoodsList = orderGoodsMapper.selectList(
                    Wrappers.lambdaQuery(OrderGoods.class)
                            .eq(OrderGoods::getOrderId, o.getId())
                            .eq(OrderGoods::getIsDelete, 0)
            );
            vo.setGoodsList(orderGoodsList);
            vo.setGoodsCount(orderGoodsList.stream().mapToInt(OrderGoods::getNumber).sum());

            // 查询用户信息
            User user = userMapper.selectById(o.getUserId());
            if (user != null) {
                user.setNickname(new String(Base64.getDecoder().decode(user.getNickname().getBytes(StandardCharsets.UTF_8))));
            } else {
                user = new User();
                user.setNickname("已删除");
            }
            vo.setUserInfo(user);
            // 获取地区信息
            String provinceName = this.getRegionName(o.getProvince());
            String cityName = this.getRegionName(o.getCity());
            String districtName = this.getRegionName(o.getDistrict());
            vo.setFullRegion(provinceName + cityName + districtName);
            vo.setPostscript(new String(Base64.getDecoder().decode(o.getPostscript().getBytes(StandardCharsets.UTF_8))));

            // 处理订单备注和时间格式
            vo.setAddTime(o.getAddTime());
            vo.setPayTime(o.getPayTime());

            // 查询订单状态文本
            vo.setOrderStatusText(this.getOrderStatusText(o.getId()));

            // 查询物流信息
            OrderExpress express = orderExpressMapper.selectOne(
                    Wrappers.lambdaQuery(OrderExpress.class).eq(OrderExpress::getOrderId, o.getId())
            );
            vo.setExpressInfo(express != null ? express.getShipperName() + express.getLogisticCode() : "");

        }).orElse(Collections.emptyList());

        return ConvertUtils.convert(orderPage, PageResult<OrderAdminVo>::new, (s, t) -> t.setRecords(orderVoList)).orElse(new PageResult<>());
    }

    /**
     * 获取区域名称（存在多个取第一条）
     *
     * @param regionId 区域id
     * @return String 区域名称
     */
    public String getRegionName(Integer regionId) {
        return regionMapper.selectList(Wrappers.lambdaQuery(Region.class).eq(Region::getId, regionId)).get(0).getName();
    }

    public String getOrderStatusText(Integer orderId) {
        // 查询订单信息
        Order orderInfo = orderMapper.selectById(orderId);
        if (orderInfo == null) {
            return "订单不存在";
        }

        // 根据订单状态设置状态文本
        return switch (orderInfo.getOrderStatus()) {
            case 101 -> "待付款";
            case 102, 103 -> "交易关闭"; // 到时间系统自动取消
            case 201 -> "待备货";
            case 300 -> "待发货";
            case 301 -> "已发货";
            case 302, 303 -> "待评价"; // 到时间，未收货的系统自动收货
            case 401 -> "交易成功"; // 到时间，未收货的系统自动收货
            default -> "未知状态";
        };
    }

    public Integer getAutoStatus() {
        Settings settings = settingsMapper.selectById(1);
        return settings != null ? settings.getAutoDelivery() : 0;
    }

    public PageResult<OrderAdminVo> toDelivery(int page, int size, String status) {
        Page<Order> orderPage = orderMapper.selectPage(new Page<>(page, size),
                Wrappers.lambdaQuery(Order.class)
                        .eq(Order::getOrderStatus, status)
                        .orderByDesc(Order::getId)
        );

        List<OrderAdminVo> orderVoList = (List<OrderAdminVo>) ConvertUtils.convertCollection(orderPage.getRecords(), OrderAdminVo::new, (o, vo) -> {
            // 查询订单商品信息
            List<OrderGoods> orderGoodsList = orderGoodsMapper.selectList(
                    Wrappers.lambdaQuery(OrderGoods.class)
                            .eq(OrderGoods::getOrderId, o.getId())
                            .eq(OrderGoods::getIsDelete, 0)
            );
            vo.setGoodsList(orderGoodsList);
            vo.setGoodsCount(orderGoodsList.stream().mapToInt(OrderGoods::getNumber).sum());
            // 获取地区信息
            String provinceName = this.getRegionName(o.getProvince());
            String cityName = this.getRegionName(o.getCity());
            String districtName = this.getRegionName(o.getDistrict());
            vo.setFullRegion(provinceName + cityName + districtName);
            vo.setPostscript(new String(Base64.getDecoder().decode(o.getPostscript().getBytes(StandardCharsets.UTF_8))));

            // 处理订单备注和时间格式
            vo.setAddTime(o.getAddTime());
            vo.setPayTime(o.getPayTime());

            // 查询订单状态文本
            vo.setOrderStatusText(this.getOrderStatusText(o.getId()));

            // 获取按钮文本
            vo.setButtonText(this.getOrderBtnText(o.getId()));
        }).orElse(Collections.emptyList());

        return ConvertUtils.convert(orderPage, PageResult<OrderAdminVo>::new, (s, t) -> t.setRecords(orderVoList)).orElse(new PageResult<>());
    }

    public String getOrderBtnText(Integer orderId) {
        // 查询订单信息
        Order orderInfo = orderMapper.selectById(orderId);
        String statusText = switch (orderInfo.getOrderStatus()) {
            case 101 -> "修改价格";
            case 102, 202, 103, 203, 301, 302, 303, 401 -> "查看详情";
            case 201 -> "备货";
            case 300 -> "打印快递单";
            default -> "";
        };

        // 如果是状态 301，修改按钮文本
        if (orderInfo.getOrderStatus() == 301) {
            statusText = "确认收货";
        }

        return statusText;
    }

    @Transactional(rollbackFor = Exception.class)
    public String saveGoodsList(SaveGoodsListDto dto) {
        BigDecimal changePrice = BigDecimal.valueOf(dto.getNumber()).multiply(dto.getRetailPrice());
        OrderGoods orderGoods = orderGoodsMapper.selectById(dto.getId());
        Order order = orderMapper.selectById(dto.getOrderId());
        if (dto.getAddOrMinus() == 0) {

            // 减少商品数量
            orderGoodsMapper.update(Wrappers.lambdaUpdate(OrderGoods.class)
                    .eq(OrderGoods::getId, dto.getId())
                    .set(OrderGoods::getNumber, orderGoods.getNumber() - dto.getNumber()));
            // 更新订单价格
            orderMapper.update(Wrappers.lambdaUpdate(Order.class)
                    .eq(Order::getId, dto.getOrderId())
                    .set(Order::getActualPrice, order.getActualPrice().subtract(changePrice))
                    .set(Order::getOrderPrice, order.getOrderPrice().subtract(changePrice))
                    .set(Order::getGoodsPrice, order.getGoodsPrice().subtract(changePrice)));
        } else if (dto.getAddOrMinus() == 1) {
            // 增加商品数量
            orderGoodsMapper.update(Wrappers.lambdaUpdate(OrderGoods.class)
                    .eq(OrderGoods::getId, dto.getId())
                    .set(OrderGoods::getNumber, orderGoods.getNumber() + dto.getNumber()));

            // 更新订单价格
            orderMapper.update(Wrappers.lambdaUpdate(Order.class)
                    .eq(Order::getId, dto.getOrderId())
                    .set(Order::getActualPrice, order.getActualPrice().add(changePrice))
                    .set(Order::getOrderPrice, order.getOrderPrice().add(changePrice))
                    .set(Order::getGoodsPrice, order.getGoodsPrice().add(changePrice)));
        } else {
            return "";
        }

        // 生成新的订单号
        String orderSn = generateOrderNumber();
        orderMapper.update(Wrappers.lambdaUpdate(Order.class)
                .eq(Order::getId, dto.getOrderId())
                .set(Order::getOrderSn, orderSn));

        return orderSn;
    }

    public static String generateOrderNumber() {
        LocalDateTime now = LocalDateTime.now();
        String dateTimeStr = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int randomNum = ThreadLocalRandom.current().nextInt(100000, 999999);
        return dateTimeStr + randomNum;
    }

    @Transactional(rollbackFor = Exception.class)
    public String goodsListDelete(SaveGoodsListDto dto) {
        BigDecimal changePrice = dto.getRetailPrice().multiply(BigDecimal.valueOf(dto.getNumber()));

        // 更新订单商品：设置 is_delete = 1
        orderGoodsMapper.update(Wrappers.lambdaUpdate(OrderGoods.class)
                .set(OrderGoods::getIsDelete, 1)
                .eq(OrderGoods::getId, dto.getId()));

        Order order = orderMapper.selectById(dto.getOrderId());

        // 更新订单价格
        orderMapper.update(Wrappers.lambdaUpdate(Order.class)
                .eq(Order::getId, dto.getOrderId())
                .set(Order::getActualPrice, order.getActualPrice().subtract(changePrice))
                .set(Order::getOrderPrice, order.getOrderPrice().subtract(changePrice))
                .set(Order::getGoodsPrice, order.getGoodsPrice().subtract(changePrice)));

        // 生成新的订单号
        String orderSn = generateOrderNumber();
        orderMapper.update(Wrappers.lambdaUpdate(Order.class)
                .eq(Order::getId, dto.getOrderId())
                .set(Order::getOrderSn, orderSn));

        return orderSn;
    }

    public Integer saveAdminMemo(SaveAdminMemoDto dto) {
        return orderMapper.update(Wrappers.lambdaUpdate(Order.class)
                .set(Order::getAdminMemo, dto.getText())
                .eq(Order::getId, dto.getId()));
    }

    public Integer savePrintInfo(SaveAdminMemoDto dto) {
        return orderMapper.update(Wrappers.lambdaUpdate(Order.class)
                .set(Order::getPrintInfo, dto.getPrintInfo())
                .eq(Order::getId, dto.getId()));
    }

    public Integer saveExpressValueInfo(SaveAdminMemoDto dto) {
        return orderMapper.update(Wrappers.lambdaUpdate(Order.class)
                .set(Order::getExpressValue, dto.getExpressValue())
                .eq(Order::getId, dto.getId()));
    }

    public Integer saveRemarkInfo(SaveAdminMemoDto dto) {
        return orderMapper.update(Wrappers.lambdaUpdate(Order.class)
                .set(Order::getRemark, dto.getRemark())
                .eq(Order::getId, dto.getId()));
    }

    public OrderDetailAdminVo getOrderDetail(Integer orderId) {
        OrderDetailAdminVo result = new OrderDetailAdminVo();
        // 获取订单数据
        Order order = orderMapper.selectById(orderId);
        List<OrderGoods> orderGoodsList = orderGoodsMapper.selectList(
                Wrappers.lambdaQuery(OrderGoods.class)
                        .eq(OrderGoods::getOrderId, orderId)
                        .eq(OrderGoods::getIsDelete, 0)
        );
        OrderAdminVo orderAdminVo = ConvertUtils.convert(order, OrderAdminVo::new).orElseThrow();
        orderAdminVo.setGoodsList(orderGoodsList);
        int goodsCount = orderGoodsList.stream().mapToInt(OrderGoods::getNumber).sum();
        orderAdminVo.setGoodsCount(goodsCount);

        List<OrderGoodsAdminVo> orderGoodsAdminVos =
                (List<OrderGoodsAdminVo>) ConvertUtils.convertCollection(orderGoodsList, OrderGoodsAdminVo::new).orElseThrow();
        // 获取商品的商品编号
        for (OrderGoodsAdminVo goods : orderGoodsAdminVos) {
            Product product = productMapper.selectById(goods.getProductId());
            if (product != null) {
                goods.setGoodsSn(product.getGoodsSn());
            }
        }

        // 获取用户信息
        User user = userMapper.selectById(order.getUserId());
        String nickname = new String(Base64.getDecoder().decode(user.getNickname()));
        orderAdminVo.setUserName(nickname);

        // 获取地区信息
        String provinceName = this.getRegionName(order.getProvince());
        String cityName = this.getRegionName(order.getCity());
        String districtName = this.getRegionName(order.getDistrict());
        orderAdminVo.setFullRegion(provinceName + cityName + districtName);
        orderAdminVo.setPostscript(new String(Base64.getDecoder().decode(order.getPostscript().getBytes(StandardCharsets.UTF_8))));
        orderAdminVo.setOrderStatusText(this.getOrderStatusText(orderId));
        orderAdminVo.setAllAddress(orderAdminVo.getFullRegion() + orderAdminVo.getAddress());

        // 获取配置数据
        Settings settings = settingsMapper.selectById(1);

        // Sender 和 Receiver 信息
        SenderInfoVo senderInfo = new SenderInfoVo();
        senderInfo.setName(orderAdminVo.getConsignee());
        senderInfo.setMobile(orderAdminVo.getMobile());
        senderInfo.setProvince(provinceName);
        senderInfo.setCity(cityName);
        senderInfo.setDistrict(districtName);
        senderInfo.setProvinceId(order.getProvince());
        senderInfo.setCityId(order.getCity());
        senderInfo.setDistrictId(order.getDistrict());
        senderInfo.setAddress(order.getAddress());

        ReceiverInfoVo receiverInfoVo = new ReceiverInfoVo();
        receiverInfoVo.setName(settings.getName());
        receiverInfoVo.setMobile(settings.getTel());
        receiverInfoVo.setProvince(settings.getProvinceName());
        receiverInfoVo.setCity(settings.getCityName());
        receiverInfoVo.setDistrict(settings.getExpAreaName());
        receiverInfoVo.setProvinceId(settings.getProvinceId());
        receiverInfoVo.setCityId(settings.getCityId());
        receiverInfoVo.setDistrictId(settings.getDistrictId());
        receiverInfoVo.setAddress(settings.getAddress());

        result.setOrderInfo(orderAdminVo);
        result.setSender(senderInfo);
        result.setReceiver(receiverInfoVo);

        return result;
    }

    public List<RegionAdminVo> getAllRegions() {
        // 查询所有省、市、区数据
        List<Region> provinces = regionMapper.selectList(Wrappers.lambdaQuery(Region.class).eq(Region::getType, 1));
        List<Region> cities = regionMapper.selectList(Wrappers.lambdaQuery(Region.class).eq(Region::getType, 2));
        List<Region> districts = regionMapper.selectList(Wrappers.lambdaQuery(Region.class).eq(Region::getType, 3));

        // 组装数据
        List<RegionAdminVo> regionTree = new ArrayList<>();
        for (Region province : provinces) {
            List<RegionAdminVo> cityList = new ArrayList<>();
            for (Region city : cities) {
                if (city.getParentId().equals(province.getId())) {
                    List<RegionAdminVo> districtList = new ArrayList<>();
                    for (Region district : districts) {
                        if (district.getParentId().equals(city.getId())) {
                            districtList.add(new RegionAdminVo(district.getId(), district.getName(), null));
                        }
                    }
                    cityList.add(new RegionAdminVo(city.getId(), city.getName(), districtList));
                }
            }
            regionTree.add(new RegionAdminVo(province.getId(), province.getName(), cityList));
        }

        return regionTree;
    }

    public void updateOrderStatusToPacked(Integer orderId) {
        orderMapper.update(Wrappers.lambdaUpdate(Order.class)
                .eq(Order::getId, orderId)
                .set(Order::getOrderStatus, 300));
    }

    public void updateOrderStatusToReceived(Integer orderId) {
        orderMapper.update(Wrappers.lambdaUpdate(Order.class)
                .eq(Order::getId, orderId)
                .set(Order::getOrderStatus, 302)
                .set(Order::getShippingTime, LocalDateTime.now()));
    }

    public void updateOrderPrice(OrderPriceUpdateDto dto) {
        String orderSn = generateOrderNumber();
        orderMapper.update(Wrappers.lambdaUpdate(Order.class)
                .eq(Order::getId, dto.getOrderId())
                .set(Order::getActualPrice, dto.getActualPrice())
                .set(Order::getFreightPrice, dto.getFreightPrice())
                .set(Order::getGoodsPrice, dto.getGoodsPrice())
                .set(Order::getOrderSn, orderSn));
    }

    public OrderExpress getOrderExpress(Integer orderId) {

        OrderExpress expressInfo = this.checkExpress(orderId);
        // 计算时间差
        boolean isLt60 = expressInfo.getUpdateTime() != null
                && Duration.between(LocalDateTime.now(), expressInfo.getUpdateTime()).toMinutes() < 20;
        // 如果is_finish == 1；或者 updateTime 小于 20分钟
        if (expressInfo.getIsFinish() || isLt60) {
            return expressInfo;
        } else {
            String shipperCode = expressInfo.getShipperCode();
            String expressNo = expressInfo.getLogisticCode();
            String code = shipperCode.substring(0, 2);
            String shipperName = "";
            // 顺丰的话需要在单号后面拼上寄件人或收件人手机号末四位
            if ("SF".equals(code)) {
                shipperName = "SFEXPRESS";
                expressNo = expressNo + ':' + "2041";
            } else {
                shipperName = shipperCode;
            }
            AliExpressResponseVo express = expressService.getExpress(shipperName, expressNo);
            if (express != null) {
                AliExpressResponseVo.Result result = express.getResult();
                String deliveryStatus = result.getDeliverystatus();
                LocalDateTime newUpdateTime = null;
                if (StringUtils.isNotBlank(result.getUpdateTime())) {
                    newUpdateTime = DateUtils.parseTime(result.getUpdateTime());
                }
                expressInfo.setExpressStatus(AliExpressDeliveryStatusEnum.fromCode(deliveryStatus).getDesc());
                expressInfo.setIsFinish("1".equals(result.getIssign()));
                expressInfo.setTraces(JSON.toJSONString(result.getList()));
                expressInfo.setUpdateTime(newUpdateTime);
                orderExpressMapper.updateById(expressInfo);
                return expressInfo;
            } else {
                throw new FruitGreetException(FruitGreetError.NO_EXPRESS_DATA);
            }
        }
    }

    public OrderExpress checkExpress(Integer orderId) {
        OrderExpress expressInfo = orderExpressMapper.selectOne(Wrappers.lambdaQuery(OrderExpress.class)
                .eq(OrderExpress::getOrderId, orderId)
        );
        if (expressInfo == null) {
            throw new FruitGreetException(FruitGreetError.NO_EXPRESS_DATA);
        }
        return expressInfo;
    }

    public OrderExpressVo directPrintExpress(Integer orderId) {
        OrderExpress expressInfo = orderExpressMapper.selectOne(Wrappers.lambdaQuery(OrderExpress.class)
                .eq(OrderExpress::getOrderId, orderId)
        );
        Shipper shipper;
        // TODO 这里不知道为什么只取两个快递的信息
        if (expressInfo.getExpressType() < 4) {
            shipper = shipperMapper.selectOne(Wrappers.lambdaQuery(Shipper.class)
                    .eq(Shipper::getCode, "SF")
            );
        } else {
            shipper = shipperMapper.selectOne(Wrappers.lambdaQuery(Shipper.class)
                    .eq(Shipper::getCode, "YTO")
            );
        }
        Shipper finalShipper = shipper;
        return ConvertUtils.convert(expressInfo, OrderExpressVo::new, (s, t) -> {
            t.setMonthCode(finalShipper.getMonthCode());
            t.setSendTime(s.getAddTime());
        }).orElseThrow();
    }

    public Integer rePrintExpress(Integer orderId) {
        return orderMapper.update(Wrappers.lambdaUpdate(Order.class)
                .eq(Order::getId, orderId)
                .set(Order::getOrderSn, generateOrderNumber())
        );
    }

    public Integer saveAddress(OrderSaveAddressDto dto) {
        return orderMapper.update(Wrappers.lambdaUpdate(Order.class)
                .eq(Order::getOrderSn, dto.getOrderSn())
                .set(Order::getConsignee, dto.getName())
                .set(Order::getMobile, dto.getMobile())
                .set(Order::getAddress, dto.getAddress())
                .set(Order::getProvince, dto.getAddOptions()[0])
                .set(Order::getCity, dto.getAddOptions()[1])
                .set(Order::getDistrict, dto.getAddOptions()[2])
        );
    }

    public Integer changeStatus(OrderChangStatusDto dto) {
        return orderMapper.update(Wrappers.lambdaUpdate(Order.class)
                .eq(Order::getOrderSn, dto.getOrderSn())
                .set(Order::getOrderStatus, dto.getStatus())
        );
    }
}
