package com.liyuyouguo.server.beans.vo.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liyuyouguo.server.entity.shop.Goods;
import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class FootprintVo {

    private Integer id;

    /**
     * 商品id
     */
    @JsonProperty("goods_id")
    private Integer goodsId;

    /**
     * 足迹创建时间
     */
    @JsonProperty("add_time")
    private String addTime;

    private Goods goods;

}
