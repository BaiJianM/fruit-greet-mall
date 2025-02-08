package com.liyuyouguo.admin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liyuyouguo.common.entity.shop.Shipper;
import com.liyuyouguo.common.mapper.ShipperMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShipperService {

    private final ShipperMapper shipperMapper;

    public List<Shipper> getEnabledShippers() {
        return shipperMapper.selectList(Wrappers.lambdaQuery(Shipper.class)
                .eq(Shipper::getEnabled, 1));
    }

}
