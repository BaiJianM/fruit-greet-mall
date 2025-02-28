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
@Schema(description = "goods 表")
public class Goods {

    /**
     * 主键
     */
    @Schema(description = "主键", example = "1")
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer categoryId;

    private Boolean isOnSale;

    private String name;

    private Integer goodsNumber;

    private Integer sellVolume;

    private String keywords;

    private String retailPrice;

    private BigDecimal minRetailPrice;

    private String costPrice;

    private BigDecimal minCostPrice;

    private String goodsBrief;

    private String goodsDesc;

    private Integer sortOrder;

    private Boolean isIndex;

    private Boolean isNew;

    private String goodsUnit;

    private String httpsPicUrl;

    private String listPicUrl;

    private Integer freightTemplateId;

    private Integer freightType;

    private Boolean isDelete;

    private Boolean hasGallery;

    private Boolean hasDone;

}