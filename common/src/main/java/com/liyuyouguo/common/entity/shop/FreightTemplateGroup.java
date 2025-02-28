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
@Schema(description = "freight_template_group 表")
public class FreightTemplateGroup {

    /**
     * 主键
     */
    @Schema(description = "主键", example = "1")
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer templateId;

    private Boolean isDefault;

    private String area;

    private Integer start;

    private BigDecimal startFee;

    private Integer add;

    private BigDecimal addFee;

    private Integer freeByNumber;

    private BigDecimal freeByMoney;

    private Boolean isDelete;

}