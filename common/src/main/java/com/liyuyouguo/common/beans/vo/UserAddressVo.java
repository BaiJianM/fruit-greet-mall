package com.liyuyouguo.common.beans.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class UserAddressVo {

    private String fullRegion;

    private Integer id;

    @Schema(description = "unknown", example = "1")
    private String name;

    @Schema(description = "unknown", example = "1")
    private Integer userId;

    @Schema(description = "unknown", example = "1")
    private Integer countryId;

    @Schema(description = "unknown", example = "1")
    private Integer provinceId;

    @Schema(description = "unknown", example = "1")
    private Integer cityId;

    @Schema(description = "unknown", example = "1")
    private Integer districtId;

    @Schema(description = "unknown", example = "1")
    private String address;

    @Schema(description = "unknown", example = "1")
    private String mobile;

    @Schema(description = "unknown", example = "1")
    private Integer isDefault;

    @Schema(description = "unknown", example = "1")
    private Integer isDelete;

}
