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
@Schema(description = "hiolabs_search_history 表")
@TableName("hiolabs_search_history")
public class SearchHistory {

    private Integer id;

    private String keyword;

    private String from;

    @JsonProperty("add_time")
    private LocalDateTime addTime;

    @JsonProperty("user_id")
    private Integer userId;
}