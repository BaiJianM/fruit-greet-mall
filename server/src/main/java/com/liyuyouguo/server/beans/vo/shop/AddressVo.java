package com.liyuyouguo.server.beans.vo.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liyuyouguo.server.beans.vo.shop.interfaces.IAddress;
import com.liyuyouguo.server.entity.shop.Address;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收货地址信息
 *
 * @author baijianmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddressVo extends Address implements IAddress {

    @JsonProperty("province_name")
    private String provinceName;

    @JsonProperty("city_name")
    private String cityName;

    @JsonProperty("district_name")
    private String districtName;

    @JsonProperty("full_region")
    private String fullRegion;

}
