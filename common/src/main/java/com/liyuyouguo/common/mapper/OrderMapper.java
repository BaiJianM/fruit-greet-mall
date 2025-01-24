package com.liyuyouguo.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liyuyouguo.common.entity.shop.Order;
import org.springframework.stereotype.Repository;

/**
 * 订单Mapper
 *
 * @author baijianmin
 */
@Repository
public interface OrderMapper extends BaseMapper<Order> {
}
