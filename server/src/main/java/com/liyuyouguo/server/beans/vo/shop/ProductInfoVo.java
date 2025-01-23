package com.liyuyouguo.server.beans.vo.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liyuyouguo.server.entity.shop.GoodsSpecification;
import lombok.Data;

import java.util.List;

/**
 * @author baijianmin
 */
@Data
public class ProductInfoVo {

    @JsonProperty("specification_id")
    private Integer specificationId;

    private String name;

    private List<GoodsSpecification> valueList;

}
