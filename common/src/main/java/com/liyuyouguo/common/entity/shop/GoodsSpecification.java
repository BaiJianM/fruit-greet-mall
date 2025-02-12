package com.liyuyouguo.common.entity.shop;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "hiolabs_goods_specification 表")
@TableName("hiolabs_goods_specification")
public class GoodsSpecification {

    private Integer id;

    @JsonProperty("goods_id")
    private Integer goodsId;

    @JsonProperty("specification_id")
    private Integer specificationId;

    private String value;

    @JsonProperty("pic_url")
    private String picUrl;

    @JsonProperty("is_delete")
    private Boolean isDelete;

}