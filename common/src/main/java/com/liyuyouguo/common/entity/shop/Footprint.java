package com.liyuyouguo.common.entity.shop;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户足迹表
 *
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户足迹表")
@TableName("hiolabs_footprint")
public class Footprint {

    private Integer id;

    /**
     * 用户id
     */
    @JsonProperty("user_id")
    private Integer userId;

    /**
     * 商品id
     */
    @JsonProperty("goods_id")
    private Integer goodsId;

    /**
     * 足迹创建时间
     */
    @JsonProperty("add_time")
    private LocalDateTime addTime;

}