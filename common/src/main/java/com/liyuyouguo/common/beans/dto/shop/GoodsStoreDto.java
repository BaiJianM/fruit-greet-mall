package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

import java.util.List;

/**
 * @author baijianmin
 */
@Data
public class GoodsStoreDto {

    private GoodsDto info;

    private List<GoodsSpecificationDto> specData;

    private Integer specValue;

    private Integer cateId;

}
