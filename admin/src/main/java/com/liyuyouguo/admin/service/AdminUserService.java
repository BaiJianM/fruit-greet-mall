package com.liyuyouguo.admin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.beans.dto.shop.SaveAddressDto;
import com.liyuyouguo.common.beans.dto.shop.UpdateUserInfoDto;
import com.liyuyouguo.common.beans.dto.shop.UserStoreDto;
import com.liyuyouguo.common.beans.vo.OrderAdminVo;
import com.liyuyouguo.common.beans.vo.UserAddressVo;
import com.liyuyouguo.common.beans.vo.UserDataInfoVo;
import com.liyuyouguo.common.entity.shop.*;
import com.liyuyouguo.common.mapper.*;
import com.liyuyouguo.common.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserMapper userMapper;

    private final OrderMapper orderMapper;

    private final CartMapper cartMapper;

    private final AddressMapper addressMapper;

    private final RegionMapper regionMapper;

    private final FootprintMapper footprintMapper;

    private final OrderGoodsMapper orderGoodsMapper;

    private final OrderService orderService;

    public PageResult<User> getUserList(int page, int size, String nickname) {
        String encodedNickname = Base64.getEncoder().encodeToString(nickname.getBytes());

        Page<User> users = userMapper.selectPage(
                new Page<>(page, size),
                Wrappers.lambdaQuery(User.class)
                        .like(User::getNickname, encodedNickname)
                        .orderByDesc(User::getId)
        );
        users.getRecords().forEach(u -> u.setNickname(new String(Base64.getDecoder().decode(u.getNickname()))));
        return ConvertUtils.convert(users, PageResult<User>::new).orElseThrow();
    }

    public User getUserInfo(Integer id) {
        User user = userMapper.selectById(id);
        user.setNickname(new String(Base64.getDecoder().decode(user.getNickname())));
        return user;
    }

    public UserDataInfoVo getUserDataInfo(Integer id) {
        UserDataInfoVo info = new UserDataInfoVo();
        // 获取订单总数
        long orderSum = orderMapper.selectCount(
                Wrappers.lambdaQuery(Order.class)
                        .eq(Order::getUserId, id)
                        .lt(Order::getOrderType, 8)
                        .eq(Order::getIsDelete, 0)
        );
        info.setOrderSum(orderSum);

        // 获取已完成的订单数
        long orderDone = orderMapper.selectCount(
                Wrappers.lambdaQuery(Order.class)
                        .eq(Order::getUserId, id)
                        .in(Order::getOrderStatus, Arrays.asList("302", "303", "401"))
                        .lt(Order::getOrderType, 8)
                        .eq(Order::getIsDelete, 0)
        );
        info.setOrderDone(orderDone);

        // 获取已完成订单的金额总和
        BigDecimal orderMoney = BigDecimal.valueOf(orderMapper.selectList(
                Wrappers.lambdaQuery(Order.class)
                        .eq(Order::getUserId, id)
                        .in(Order::getOrderStatus, Arrays.asList("302", "303", "401"))
                        .lt(Order::getOrderType, 8)
                        .eq(Order::getIsDelete, 0)
        ).stream().mapToDouble(o -> o.getActualPrice().doubleValue()).sum());
        info.setOrderMoney(orderMoney);

        // 获取购物车商品总数
        long cartSum = cartMapper.selectList(
                Wrappers.lambdaQuery(Cart.class)
                        .eq(Cart::getUserId, id)
                        .eq(Cart::getIsDelete, 0)
        ).stream().mapToLong(Cart::getNumber).sum();
        info.setCartSum(cartSum);

        return info;
    }

    public PageResult<UserAddressVo> getAddressList(Integer id, Integer page, Integer size) {
        Page<Address> addresses = addressMapper.selectPage(new Page<>(page, size),
                Wrappers.lambdaQuery(Address.class)
                        .eq(Address::getUserId, id));

        if (addresses.getRecords().isEmpty()) {
            return new PageResult<>();
        }
        List<UserAddressVo> userAddressVos = (List<UserAddressVo>) ConvertUtils.convertCollection(addresses.getRecords(), UserAddressVo::new, (s, t) -> {
            String province = this.getRegionName(s.getProvinceId());
            String city = this.getRegionName(s.getCityId());
            String district = this.getRegionName(s.getDistrictId());
            t.setFullRegion(province + city + district + s.getAddress());
        }).orElseThrow();
        return ConvertUtils.convert(addresses, PageResult<UserAddressVo>::new, (s, t) -> t.setRecords(userAddressVos)).orElseThrow();
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

    public void saveUserAddress(SaveAddressDto dto) {
        // 保存用户地址
        Address address = new Address();
        address.setId(dto.getId());
        address.setUserId(dto.getUserId());
        address.setName(dto.getName());
        address.setMobile(dto.getMobile());
        address.setAddress(dto.getAddress());
        address.setProvinceId(dto.getAddOptions().get(0));
        address.setCityId(dto.getAddOptions().get(1));
        address.setDistrictId(dto.getAddOptions().get(2));
        addressMapper.updateById(address);
    }

    public PageResult<Cart> getCartData(Integer userId, Integer page, Integer size) {
        Page<Cart> cartPage = cartMapper.selectPage(new Page<>(page, size), Wrappers.lambdaQuery(Cart.class)
                .eq(Cart::getUserId, userId)
                .orderByDesc(Cart::getAddTime));
        return ConvertUtils.convert(cartPage, PageResult<Cart>::new).orElseThrow();
    }


    public PageResult<Footprint> getFootprints(Integer id, Integer page, Integer size) {
        return ConvertUtils.convert(footprintMapper.getFootprintByUserId(new Page<>(page, size), id), PageResult<Footprint>::new).orElseThrow();
    }

    public PageResult<OrderAdminVo> getOrderList(Integer userId, Integer page, Integer size) {
        Page<Order> orderPage = orderMapper.selectPage(
                new Page<>(page, size),
                Wrappers.lambdaQuery(Order.class)
                        .eq(Order::getUserId, userId)
                        .lt(Order::getOrderType, 8)
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
            vo.setOrderStatusText(orderService.getOrderStatusText(o.getId()));

            // 获取按钮文本
            vo.setButtonText(orderService.getOrderBtnText(o.getId()));
        }).orElse(Collections.emptyList());
        return ConvertUtils.convert(orderPage, PageResult<OrderAdminVo>::new, (s, t) -> t.setRecords(orderVoList)).orElse(new PageResult<>());
    }

    public String getOrderStatusText(Integer orderStatus) {
        return switch (orderStatus) {
            case 101 -> "待付款";
            case 102 -> "交易关闭";
            case 103 -> "交易关闭"; // 到时间系统自动取消
            case 201 -> "待发货";
            case 202 -> "退款中";
            case 203 -> "已退款";
            case 300 -> "已备货";
            case 301 -> "已发货";
            case 302 -> "待评价";
            case 303 -> "待评价"; // 到时间，未收货的系统自动收货
            case 401 -> "交易成功"; // 到时间，未收货的系统自动收货
            case 801 -> "拼团待付款";
            case 802 -> "拼团中";
            default -> "待付款"; // 如果sum变为0了，则，变成201待发货
        };
    }

    public void updateUserInfo(UpdateUserInfoDto dto) {
        String nicknameBase64 = Base64.getEncoder().encodeToString(dto.getNickname().getBytes());
        userMapper.update(Wrappers.lambdaUpdate(User.class)
                .eq(User::getId, dto.getId())
                .set(User::getNickname, nicknameBase64));
    }


    public void updateName(UpdateUserInfoDto dto) {
        userMapper.update(Wrappers.lambdaUpdate(User.class)
                .eq(User::getId, dto.getId())
                .set(User::getName, dto.getName()));
    }

    public void updateMobile(UpdateUserInfoDto dto) {
        userMapper.update(Wrappers.lambdaUpdate(User.class)
                .eq(User::getId, dto.getId())
                .set(User::getMobile, dto.getMobile()));
    }

    public void store(UserStoreDto dto) {
        if (dto.getId() != null && dto.getId() > 0) {
            userMapper.updateById(ConvertUtils.convert(dto, User::new).orElseThrow());
        } else {
            userMapper.insert(ConvertUtils.convert(dto, User::new, (s, t) -> t.setId(null)).orElseThrow());
        }
    }

    public void destroy(Integer id) {
        userMapper.deleteById(id);
    }
}
