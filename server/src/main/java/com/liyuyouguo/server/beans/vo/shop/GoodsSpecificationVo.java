package com.liyuyouguo.server.beans.vo.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 商品规格信息
 *
 * @author baijianmin
 */
@Data
public class GoodsSpecificationVo {

    private Long id;

    @JsonProperty("goods_id")
    private Long goodsId;

    @JsonProperty("specification_id")
    private Long specificationId;

    private String value;

    @JsonProperty("pic_url")
    private String picUrl;

    @JsonProperty("is_delete")
    private Boolean isDelete;

    @JsonProperty("goods_number")
    private Integer goodsNumber;

}
