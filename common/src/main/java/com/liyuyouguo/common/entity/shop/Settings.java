package com.liyuyouguo.common.entity.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "settings 表")
public class Settings {

    /**
     * 主键
     */
    @Schema(description = "主键", example = "1")
    @TableId(type = IdType.AUTO)
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