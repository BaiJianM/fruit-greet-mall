package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class UpdatePriceDto {

    private String sn;

    private Integer id;

    private BigDecimal price;

}
