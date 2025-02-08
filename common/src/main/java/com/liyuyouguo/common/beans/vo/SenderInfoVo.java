package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("province_id")
    private Integer provinceId;

    @JsonProperty("city_id")
    private Integer cityId;

    @JsonProperty("district_id")
    private Integer districtId;

}
