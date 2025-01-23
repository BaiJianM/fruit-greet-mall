package com.liyuyouguo.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liyuyouguo.server.entity.shop.Order;
import org.springframework.stereotype.Repository;

/**
 * 订单Mapper
 *
 * @author baijianmin
 */
@Repository
public interface OrderMapper extends BaseMapper<Order> {
}
