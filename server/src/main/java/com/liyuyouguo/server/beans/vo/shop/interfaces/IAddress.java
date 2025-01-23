package com.liyuyouguo.server.beans.vo.shop.interfaces;

/**
 * 地址信息接口类
 *
 * @author baijianmin
 */
public interface IAddress {

    void setProvinceName(String provinceName);

    void setCityName(String cityName);

    void setDistrictName(String districtName);

    void setFullRegion(String fullRegion);

    Integer getProvinceId();

    Integer getCityId();

    Integer getDistrictId();

}
