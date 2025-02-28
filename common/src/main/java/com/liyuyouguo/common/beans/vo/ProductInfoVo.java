package com.liyuyouguo.common.beans.vo;

import com.liyuyouguo.common.entity.shop.GoodsSpecification;
import lombok.Data;

import java.util.List;

/**
 * @author baijianmin
 */
@Data
public class ProductInfoVo {

    private Integer specificationId;

    private String name;

    private List<GoodsSpecification> valueList;

}
