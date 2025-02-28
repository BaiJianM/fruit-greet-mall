package com.liyuyouguo.common.entity.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "product 表")
public class Product {

    /**
     * 主键
     */
    @Schema(description = "主键", example = "1")
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer goodsId;

    private String goodsSpecificationIds;

    private String goodsSn;

    private Integer goodsNumber;

    private BigDecimal retailPrice;

    private BigDecimal cost;

    private Double goodsWeight;

    private Boolean hasChange;

    private String goodsName;

    private Boolean isOnSale;

    private Boolean isDelete;

}