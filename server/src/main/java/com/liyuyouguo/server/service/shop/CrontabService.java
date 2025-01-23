package com.liyuyouguo.server.service.shop;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liyuyouguo.server.entity.shop.Ad;
import com.liyuyouguo.server.entity.shop.Notice;
import com.liyuyouguo.server.entity.shop.Order;
import com.liyuyouguo.server.entity.shop.Settings;
import com.liyuyouguo.server.mapper.AdMapper;
import com.liyuyouguo.server.mapper.NoticeMapper;
import com.liyuyouguo.server.mapper.OrderMapper;
import com.liyuyouguo.server.mapper.SettingsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 定时任务服务类
 *
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CrontabService {

    private final OrderMapper orderMapper;

    private final NoticeMapper noticeMapper;

    private final AdMapper adMapper;

    private SettingsMapper settingsMapper;

    // 每1分钟执行一次
    @Scheduled(fixedRate = 60 * 1000)
    public void timeTask() {
        System.out.println("=============开始============");

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime newDay = LocalDateTime.now().withHour(3).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime newDayOver = newDay.plusSeconds(59);

        if (currentTime.isAfter(newDay) && currentTime.isBefore(newDayOver)) {
            // 在凌晨3:00至3:00:59期间执行操作（若有必要）
        }

        // 将公告下掉
        List<Notice> notices = noticeMapper.selectList(
                Wrappers.lambdaQuery(Notice.class)
                        .eq(Notice::getIsDelete, 0)
        );
        for (Notice notice : notices) {
            LocalDateTime noticeEndTime = notice.getEndTime();
            if (currentTime.isAfter(noticeEndTime)) {
                noticeMapper.update(Wrappers.lambdaUpdate(Notice.class)
                                .set(Notice::getIsDelete, 1)
                                .eq(Notice::getId, notice.getId())
                );
            }
        }

        // 处理订单状态更新
        LocalDateTime expireTime = currentTime.minusDays(1);
        List<Order> orderList = orderMapper.selectList(
                Wrappers.lambdaQuery(Order.class)
                        .in(Order::getOrderStatus, Arrays.asList(101, 801))
                        .lt(Order::getAddTime, expireTime)
                        .eq(Order::getIsDelete, 0)
        );
        for (Order order : orderList) {
            orderMapper.update(Wrappers.lambdaUpdate(Order.class)
                            .set(Order::getOrderStatus, 102)
                            .eq(Order::getId, order.getId())
            );
        }

        // 定时将到期的广告停掉
        List<Ad> adInfo = adMapper.selectList(Wrappers.lambdaQuery(Ad.class)
                        .lt(Ad::getEndTime, currentTime)
                        .eq(Ad::getEnabled, 1)
        );
        for (Ad ad : adInfo) {
            adMapper.update(Wrappers.lambdaUpdate(Ad.class)
                            .set(Ad::getEnabled, 0)
                            .eq(Ad::getId, ad.getId())
            );
        }

        // 定时将长时间未收货的订单设置为确认收货
        LocalDateTime noConfirmTime = currentTime.minusDays(5);
        List<Order> noConfirmList = orderMapper.selectList(
                Wrappers.lambdaQuery(Order.class)
                        .eq(Order::getOrderStatus, 301)
                        .le(Order::getShippingTime, noConfirmTime)
//                        .ne(Order::getShippingTime, null)
                        .eq(Order::getIsDelete, 0)
        );
        for (Order order : noConfirmList) {
            orderMapper.update(Wrappers.lambdaUpdate(Order.class)
                            .set(Order::getOrderStatus, 401)
                            .set(Order::getConfirmTime, currentTime)
                            .eq(Order::getId, order.getId())
            );
        }
    }

//    @Scheduled(fixedRate = 10 * 1000)
    public void resetSql() {
        // 当前时间 + 300 秒
        LocalDateTime currentTime = LocalDateTime.now().plusSeconds(300);

        // 查询数据库中的设置记录
        Settings info = settingsMapper.selectById(1);

        if (info != null && info.getReset() == 0) {
            // 更新 countdown 和 reset 字段
            Settings updatedSettings = new Settings();
            updatedSettings.setId(1);
            updatedSettings.setCountdown(currentTime);
            updatedSettings.setReset(1);

            settingsMapper.updateById(updatedSettings);
            log.info("重置了！");
        } else {
            log.info("还没到呢！");
        }
    }

}
