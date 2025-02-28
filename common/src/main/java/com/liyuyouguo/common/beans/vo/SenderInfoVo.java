package com.liyuyouguo.common.beans.vo;

import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class SenderInfoVo {

    private String name;

    private String mobile;

    private String province;

    private String city;

    private String district;

    private String address;

    private Integer provinceId;

    private Integer cityId;

    private Integer districtId;

}
