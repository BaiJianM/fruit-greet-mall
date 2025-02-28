package com.liyuyouguo.common.beans.vo;

import com.liyuyouguo.common.beans.vo.interfaces.IAddress;
import com.liyuyouguo.common.entity.shop.Address;
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

    private String provinceName;

    private String cityName;

    private String districtName;

    private String fullRegion;

}
