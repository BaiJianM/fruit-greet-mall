package com.liyuyouguo.common.beans.vo;

import lombok.Data;

/**
 * 商品规格信息
 *
 * @author baijianmin
 */
@Data
public class GoodsSpecificationVo {

    private Long id;

    private Long goodsId;

    private Long specificationId;

    private String value;

    private String picUrl;

    private Boolean isDelete;

    private Integer goodsNumber;

}
