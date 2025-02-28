package com.liyuyouguo.common.entity.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "cart表")
public class Cart {

    /**
     * 主键
     */
    @Schema(description = "主键", example = "1")
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer goodsId;

    private String goodsSn;

    private Integer productId;

    private String goodsName;

    private String goodsAka;

    private Double goodsWeight;

    private BigDecimal addPrice;

    private BigDecimal retailPrice;

    private Integer number;

    private String goodsSpecifitionNameValue;

    private String goodsSpecifitionIds;

    private Boolean checked;

    private String listPicUrl;

    private Integer freightTemplateId;

    private Boolean isOnSale;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addTime;

    private Boolean isFast;

    private Boolean isDelete;

}