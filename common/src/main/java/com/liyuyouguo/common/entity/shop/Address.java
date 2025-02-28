package com.liyuyouguo.common.entity.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地址信息表
 *
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "address表")
public class Address {

    /**
     * 主键
     */
    @Schema(description = "主键", example = "1")
    @TableId(type = IdType.AUTO)
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
    private Boolean isDefault;

    @Schema(description = "unknown", example = "1")
    private Boolean isDelete;

}