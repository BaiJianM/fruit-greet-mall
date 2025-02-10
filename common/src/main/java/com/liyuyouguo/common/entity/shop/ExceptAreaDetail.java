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
@Schema(description = "hiolabs_except_area_detail 表")
@TableName("hiolabs_except_area_detail")
public class ExceptAreaDetail {

    private Integer id;

    @JsonProperty("except_area_id")
    private Integer exceptAreaId;

    private String area;

    @JsonProperty("is_delete")
    private Integer isDelete;

}