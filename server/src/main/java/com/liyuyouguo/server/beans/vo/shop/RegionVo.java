package com.liyuyouguo.server.beans.vo.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class RegionVo {

    @JsonProperty("province_id")
    private Integer provinceId;

    @JsonProperty("city_id")
    private Integer cityId;

    @JsonProperty("country_id")
    private Integer countryId;

}
