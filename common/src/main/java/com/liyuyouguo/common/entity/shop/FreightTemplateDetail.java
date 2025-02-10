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
@Schema(description = "hiolabs_freight_template_detail 表")
@TableName("hiolabs_freight_template_detail")
public class FreightTemplateDetail {

    private Integer id;

    @JsonProperty("template_id")
    private Integer templateId;

    @JsonProperty("group_id")
    private Integer groupId;

    private String area;

    @JsonProperty("is_delete")
    private Integer isDelete;

}