package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.liyuyouguo.common.beans.vo.interfaces.IAddress;
import com.liyuyouguo.common.entity.shop.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 订单收货地址信息
 *
 * @author baijianmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderInfoVo extends Order implements IAddress {

    private String provinceName;

    private String cityName;

    private String districtName;

    private String fullRegion;

    private String orderStatusText;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmRemainTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finalPayTime;

    @Override
    public Integer getProvinceId() {
        return super.getProvince();
    }

    @Override
    public Integer getCityId() {
        return super.getCity();
    }

    @Override
    public Integer getDistrictId() {
        return super.getDistrict();
    }
}
