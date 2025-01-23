package com.liyuyouguo.server.entity.shop;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "hiolabs_formid 表")
@TableName("hiolabs_formid")
public class FormId {

    private Integer id;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("order_id")
    private Integer orderId;

    @JsonProperty("form_id")
    private Integer formId;

    @JsonProperty("add_time")
    private LocalDateTime addTime;

    @JsonProperty("use_times")
    private Integer useTimes;

}