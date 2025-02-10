package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class UserAddressVo {

    @JsonProperty("full_region")
    private String fullRegion;

    private Integer id;

    @Schema(description = "unknown", example = "1")
    private String name;

    @Schema(description = "unknown", example = "1")
    @JsonProperty("user_id")
    private Integer userId;

    @Schema(description = "unknown", example = "1")
    @JsonProperty("country_id")
    private Integer countryId;

    @Schema(description = "unknown", example = "1")
    @JsonProperty("province_id")
    private Integer provinceId;

    @Schema(description = "unknown", example = "1")
    @JsonProperty("city_id")
    private Integer cityId;

    @Schema(description = "unknown", example = "1")
    @JsonProperty("district_id")
    private Integer districtId;

    @Schema(description = "unknown", example = "1")
    private String address;

    @Schema(description = "unknown", example = "1")
    private String mobile;

    @Schema(description = "unknown", example = "1")
    @JsonProperty("is_default")
    private Integer isDefault;

    @Schema(description = "unknown", example = "1")
    @JsonProperty("is_delete")
    private Integer isDelete;

}
