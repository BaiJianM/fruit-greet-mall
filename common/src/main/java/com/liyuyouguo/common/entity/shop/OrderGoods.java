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
@Schema(description = "order_goods 表")
public class OrderGoods {

    /**
     * 主键
     */
    @Schema(description = "主键", example = "1")
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer orderId;

    private Integer goodsId;

    private String goodsName;

    private String goodsAka;

    private Integer productId;

    private Integer number;

    private BigDecimal retailPrice;

    private String goodsSpecifitionNameValue;

    private String goodsSpecifitionIds;

    private String listPicUrl;

    private Integer userId;

    private Boolean isDelete;

}