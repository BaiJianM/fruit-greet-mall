package com.liyuyouguo.common.beans.vo;

import com.liyuyouguo.common.entity.shop.Goods;
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
    private Integer goodsId;

    /**
     * 足迹创建时间
     */
    private String addTime;

    private Goods goods;

}
