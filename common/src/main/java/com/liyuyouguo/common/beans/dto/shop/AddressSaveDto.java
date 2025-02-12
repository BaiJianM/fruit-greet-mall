package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

/**
 * 保存收货地址传参
 *
 * @author baijianmin
 */
@Data
public class AddressSaveDto {

    private Integer addressId;

    private String name;

    private String mobile;

    private Integer provinceId;

    private Integer cityId;

    private Integer districtId;

    private String address;

    private Integer userId;

    private Boolean isDefault;

}
