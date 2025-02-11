package com.liyuyouguo.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liyuyouguo.common.beans.vo.DashboardInfoVo;
import com.liyuyouguo.common.beans.vo.DashboardVo;
import com.liyuyouguo.common.entity.shop.*;
import com.liyuyouguo.common.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IndexService {

    private final GoodsMapper goodsMapper;

    private final OrderMapper orderMapper;

    private final SettingsMapper settingsMapper;

    private final UserMapper userMapper;

    private final CartMapper cartMapper;

    public DashboardVo getDashboardInfo() {
        Long goodsOnSale = goodsMapper.selectCount(Wrappers.lambdaQuery(Goods.class)
                .eq(Goods::getIsOnSale, 1)
                .eq(Goods::getIsDelete, 0));

        Long orderToDelivery = orderMapper.selectCount(Wrappers.lambdaQuery(Order.class)
                .eq(Order::getOrderStatus, 300));

        Long userCount = userMapper.selectCount(null);

        Settings settings = settingsMapper.selectOne(Wrappers.lambdaQuery(Settings.class)
                .select(Settings::getCountdown));

        DashboardVo info = new DashboardVo();
        info.setUser(userCount);
        info.setGoodsOnsale(goodsOnSale);
        info.setTimestamp(settings.getCountdown().toEpochSecond(ZoneOffset.ofHours(8)));
        info.setOrderToDelivery(orderToDelivery);

        return info;
    }

    public DashboardInfoVo getDashboardInfo(Integer pindex) {
        LocalDateTime todayTimeStamp = LocalDateTime.now().toLocalDate().atStartOfDay();  // 今天零点的时间戳
        LocalDateTime yesTimeStamp = LocalDateTime.now().minusDays(1).toLocalDate().atStartOfDay();  // 昨天零点的时间戳
        LocalDateTime sevenTimeStamp = LocalDateTime.now().minusDays(7).toLocalDate().atStartOfDay();  // 7天前零点的时间戳
        LocalDateTime thirtyTimeStamp = LocalDateTime.now().minusDays(30).toLocalDate().atStartOfDay();  // 30天前零点的时间戳

        long newUser = 0, oldUser = 0, addCart = 0, addOrderNum = 0, payOrderNum = 0;
        BigDecimal addOrderSum = BigDecimal.ZERO;
        BigDecimal payOrderSum = BigDecimal.ZERO;
        List<User> newData = new ArrayList<>();
        List<User> oldData = new ArrayList<>();

        switch (pindex) {
            case 0:
                newData = userMapper.selectList(Wrappers.lambdaQuery(User.class)
                        .gt(User::getId, 0)
                        .gt(User::getRegisterTime, todayTimeStamp)
                );
                newUser = newData.size();
                newData.forEach(u -> u.setNickname(new String(Base64.getDecoder().decode(u.getNickname().getBytes()))));

                oldData = userMapper.selectList(Wrappers.lambdaQuery(User.class)
                        .gt(User::getId, 0)
                        .lt(User::getRegisterTime, todayTimeStamp)
                        .gt(User::getLastLoginTime, todayTimeStamp)
                );
                oldData.forEach(u -> u.setNickname(new String(Base64.getDecoder().decode(u.getNickname().getBytes()))));
                oldUser = oldData.size();

                addCart = cartMapper.selectCount(Wrappers.lambdaQuery(Cart.class)
                        .eq(Cart::getIsDelete, 0)
                        .gt(Cart::getAddTime, todayTimeStamp)
                );

                LambdaQueryWrapper<Order> addOrderWrapper = Wrappers.lambdaQuery(Order.class)
                        .eq(Order::getIsDelete, 0)
                        .gt(Order::getAddTime, todayTimeStamp);
                addOrderNum = orderMapper.selectCount(addOrderWrapper);

                addOrderSum = BigDecimal.valueOf(orderMapper.selectList(addOrderWrapper).stream().mapToDouble(o -> o.getActualPrice().doubleValue()).sum());

                LambdaQueryWrapper<Order> payOrderWrapper = addOrderWrapper.in(Order::getOrderStatus, Arrays.asList(201, 802, 300, 301));
                payOrderNum = orderMapper.selectCount(payOrderWrapper);

                payOrderSum = BigDecimal.valueOf(orderMapper.selectList(payOrderWrapper).stream().mapToDouble(o -> o.getActualPrice().doubleValue()).sum());

                break;
            case 1:

                newData = userMapper.selectList(Wrappers.lambdaQuery(User.class)
                        .gt(User::getId, 0)
                        .between(User::getRegisterTime, yesTimeStamp, todayTimeStamp)
                );
                newUser = newData.size();
                newData.forEach(u -> u.setNickname(new String(Base64.getDecoder().decode(u.getNickname().getBytes()))));

                oldData = userMapper.selectList(Wrappers.lambdaQuery(User.class)
                        .gt(User::getId, 0)
                        .lt(User::getRegisterTime, yesTimeStamp)
                        .between(User::getLastLoginTime, yesTimeStamp, todayTimeStamp)
                );
                oldData.forEach(u -> u.setNickname(new String(Base64.getDecoder().decode(u.getNickname().getBytes()))));
                oldUser = oldData.size();

                addCart = cartMapper.selectCount(Wrappers.lambdaQuery(Cart.class)
                        .eq(Cart::getIsDelete, 0)
                        .between(Cart::getAddTime, yesTimeStamp, todayTimeStamp)
                );

                LambdaQueryWrapper<Order> addOrderWrapper1 = Wrappers.lambdaQuery(Order.class)
                        .eq(Order::getIsDelete, 0)
                        .between(Order::getAddTime, yesTimeStamp, todayTimeStamp);
                addOrderNum = orderMapper.selectCount(addOrderWrapper1);

                addOrderSum = BigDecimal.valueOf(orderMapper.selectList(addOrderWrapper1).stream().mapToDouble(o -> o.getActualPrice().doubleValue()).sum());

                LambdaQueryWrapper<Order> payOrderWrapper1 = addOrderWrapper1.in(Order::getOrderStatus, Arrays.asList(201, 802, 300, 301));
                payOrderNum = orderMapper.selectCount(payOrderWrapper1);

                payOrderSum = BigDecimal.valueOf(orderMapper.selectList(payOrderWrapper1).stream().mapToDouble(o -> o.getActualPrice().doubleValue()).sum());

                break;
            case 2:
                newData = userMapper.selectList(Wrappers.lambdaQuery(User.class)
                        .gt(User::getId, 0)
                        .gt(User::getRegisterTime, sevenTimeStamp)
                );
                newUser = newData.size();
                newData.forEach(u -> u.setNickname(new String(Base64.getDecoder().decode(u.getNickname().getBytes()))));

                oldData = userMapper.selectList(Wrappers.lambdaQuery(User.class)
                        .gt(User::getId, 0)
                        .lt(User::getRegisterTime, sevenTimeStamp)
                        .gt(User::getLastLoginTime, sevenTimeStamp)
                );
                oldData.forEach(u -> u.setNickname(new String(Base64.getDecoder().decode(u.getNickname().getBytes()))));
                oldUser = oldData.size();

                addCart = cartMapper.selectCount(Wrappers.lambdaQuery(Cart.class)
                        .eq(Cart::getIsDelete, 0)
                        .gt(Cart::getAddTime, sevenTimeStamp)
                );

                LambdaQueryWrapper<Order> addOrderWrapper2 = Wrappers.lambdaQuery(Order.class)
                        .eq(Order::getIsDelete, 0)
                        .gt(Order::getAddTime, sevenTimeStamp);
                addOrderNum = orderMapper.selectCount(addOrderWrapper2);

                addOrderSum = BigDecimal.valueOf(orderMapper.selectList(addOrderWrapper2).stream().mapToDouble(o -> o.getActualPrice().doubleValue()).sum());

                LambdaQueryWrapper<Order> payOrderWrapper2 = addOrderWrapper2.in(Order::getOrderStatus, Arrays.asList(201, 802, 300, 301));
                payOrderNum = orderMapper.selectCount(payOrderWrapper2);

                payOrderSum = BigDecimal.valueOf(orderMapper.selectList(payOrderWrapper2).stream().mapToDouble(o -> o.getActualPrice().doubleValue()).sum());
                break;
            case 3:
                newData = userMapper.selectList(Wrappers.lambdaQuery(User.class)
                        .gt(User::getId, 0)
                        .gt(User::getRegisterTime, thirtyTimeStamp)
                );
                newUser = newData.size();
                newData.forEach(u -> u.setNickname(new String(Base64.getDecoder().decode(u.getNickname().getBytes()))));

                oldData = userMapper.selectList(Wrappers.lambdaQuery(User.class)
                        .gt(User::getId, 0)
                        .lt(User::getRegisterTime, thirtyTimeStamp)
                        .gt(User::getLastLoginTime, thirtyTimeStamp)
                );
                oldData.forEach(u -> u.setNickname(new String(Base64.getDecoder().decode(u.getNickname().getBytes()))));
                oldUser = oldData.size();

                addCart = cartMapper.selectCount(Wrappers.lambdaQuery(Cart.class)
                        .eq(Cart::getIsDelete, 0)
                        .gt(Cart::getAddTime, thirtyTimeStamp)
                );

                LambdaQueryWrapper<Order> addOrderWrapper3 = Wrappers.lambdaQuery(Order.class)
                        .eq(Order::getIsDelete, 0)
                        .gt(Order::getAddTime, thirtyTimeStamp);
                addOrderNum = orderMapper.selectCount(addOrderWrapper3);

                addOrderSum = BigDecimal.valueOf(orderMapper.selectList(addOrderWrapper3).stream().mapToDouble(o -> o.getActualPrice().doubleValue()).sum());

                LambdaQueryWrapper<Order> payOrderWrapper3 = addOrderWrapper3.in(Order::getOrderStatus, Arrays.asList(201, 802, 300, 301));
                payOrderNum = orderMapper.selectCount(payOrderWrapper3);

                payOrderSum = BigDecimal.valueOf(orderMapper.selectList(payOrderWrapper3).stream().mapToDouble(o -> o.getActualPrice().doubleValue()).sum());
                break;
        }

        return new DashboardInfoVo(newUser, oldUser, addCart, addOrderNum, addOrderSum, payOrderNum, payOrderSum, newData, oldData);
    }

}
