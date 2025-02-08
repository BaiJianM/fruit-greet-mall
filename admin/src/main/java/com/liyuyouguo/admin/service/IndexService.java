package com.liyuyouguo.admin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liyuyouguo.common.beans.vo.DashboardInfoVo;
import com.liyuyouguo.common.beans.vo.DashboardVo;
import com.liyuyouguo.common.entity.shop.Goods;
import com.liyuyouguo.common.entity.shop.Order;
import com.liyuyouguo.common.entity.shop.Settings;
import com.liyuyouguo.common.mapper.GoodsMapper;
import com.liyuyouguo.common.mapper.OrderMapper;
import com.liyuyouguo.common.mapper.SettingsMapper;
import com.liyuyouguo.common.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Date;

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
//        long todayTimeStamp = new DateTime().withTimeAtStartOfDay().getMillis() / 1000;  // 今天零点的时间戳
//        long yesTimeStamp = todayTimeStamp - 86400;  // 昨天零点的时间戳
//        long sevenTimeStamp = todayTimeStamp - 86400 * 7;  // 7天前零点的时间戳
//        long thirtyTimeStamp = todayTimeStamp - 86400 * 30;  // 30天前零点的时间戳
//
//        int newUser = 0, oldUser = 0, addCart = 0, addOrderNum = 0, addOrderSum = 0, payOrderNum = 0, payOrderSum = 0;
//        List<User> newData = new ArrayList<>();
//        List<User> oldData = new ArrayList<>();
//
//        switch (pindex) {
//            case 0:
//                newData = userMapper.selectByRegistrationTimeGreaterThan(todayTimeStamp);
//                newUser = newData.size();
//                oldData = userMapper.selectByLoginTimeGreaterThan(todayTimeStamp);
//                oldUser = oldData.size();
//                break;
//            case 1:
//                newData = userMapper.selectByRegistrationTimeBetween(yesTimeStamp, todayTimeStamp);
//                newUser = newData.size();
//                oldData = userMapper.selectByLoginTimeBetween(yesTimeStamp, todayTimeStamp);
//                oldUser = oldData.size();
//                break;
//            case 2:
//                newData = userMapper.selectByRegistrationTimeGreaterThan(sevenTimeStamp);
//                newUser = newData.size();
//                oldData = userMapper.selectByLoginTimeGreaterThan(sevenTimeStamp);
//                oldUser = oldData.size();
//                break;
//            case 3:
//                newData = userMapper.selectByRegistrationTimeGreaterThan(thirtyTimeStamp);
//                newUser = newData.size();
//                oldData = userMapper.selectByLoginTimeGreaterThan(thirtyTimeStamp);
//                oldUser = oldData.size();
//                break;
//        }
//
//        addCart = cartMapper.countByAddTimeGreaterThan(todayTimeStamp);
//        addOrderNum = orderMapper.countByAddTimeGreaterThan(todayTimeStamp);
//        addOrderSum = orderMapper.sumByAddTimeGreaterThan(todayTimeStamp);
//        payOrderNum = orderMapper.countByAddTimeGreaterThanAndOrderStatus(todayTimeStamp);
//        payOrderSum = orderMapper.sumByAddTimeGreaterThanAndOrderStatus(todayTimeStamp);
//
//        if (addOrderSum == null) {
//            addOrderSum = 0;
//        }
//        if (payOrderSum == null) {
//            payOrderSum = 0;
//        }
//
//        // Format date fields for new and old users
//        newData.forEach(user -> {
//            user.setRegisterTime(formatDate(user.getRegisterTime()));
//            user.setLastLoginTime(formatDate(user.getLastLoginTime()));
//        });
//        oldData.forEach(user -> {
//            user.setRegisterTime(formatDate(user.getRegisterTime()));
//            user.setLastLoginTime(formatDate(user.getLastLoginTime()));
//        });
//
//        return new DashboardInfoVo(newUser, oldUser, addCart, addOrderNum, addOrderSum, payOrderNum, payOrderSum, newData, oldData);
        return null;
    }

    private String formatDate(long timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp * 1000));
    }

}
