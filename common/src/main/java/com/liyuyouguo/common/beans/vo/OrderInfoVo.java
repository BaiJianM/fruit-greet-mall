package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("province_name")
    private String provinceName;

    @JsonProperty("city_name")
    private String cityName;

    @JsonProperty("district_name")
    private String districtName;

    @JsonProperty("full_region")
    private String fullRegion;

    @JsonProperty("order_status_text")
    private String orderStatusText;

    @JsonProperty("confirm_remainTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmRemainTime;

    @JsonProperty("final_pay_time")
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
