package com.liyuyouguo.common.beans.dto.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class GoodsGalleryDto {

    @JsonProperty("goods_id")
    private Integer goodsId;

    private String url;

}
