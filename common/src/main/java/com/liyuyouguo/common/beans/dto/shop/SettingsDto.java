package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author baijianmin
 */
@Data
public class SettingsDto {

    private Integer id;

    private Integer autoDelivery;

    private String name;

    private String tel;

    private String provinceName;

    private String cityName;

    private String expAreaName;

    private String address;

    private Integer discoveryImgHeight;

    private String discoveryImg;

    private Integer goodsId;

    private Integer cityId;

    private Integer provinceId;

    private Integer districtId;

    private LocalDateTime countdown;

    private Integer reset;

}
