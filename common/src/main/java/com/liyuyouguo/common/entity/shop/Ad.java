package com.liyuyouguo.common.entity.shop;

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
@Schema(description = "ad表")
@TableName("hiolabs_ad")
public class Ad {

    private Integer id;

    /**
     * 0-商品，1-链接
     */
    @Schema(description = "链接类型", example = "0")
    @JsonProperty("link_type")
    private Integer linkType;

    /**
     * 链接地址
     */
    @Schema(description = "链接地址", example = "https://www.baidu.com")
    private String link;

    /**
     * 商品id
     */
    @Schema(description = "unknown", example = "1")
    @JsonProperty("goods_id")
    private Integer goodsId;

    @Schema(description = "unknown", example = "1")
    @JsonProperty("image_url")
    private String imageUrl;

    @Schema(description = "unknown", example = "1")
    @JsonProperty("end_time")
    private LocalDateTime endTime;

    @Schema(description = "unknown", example = "1")
    private Boolean enabled;

    @Schema(description = "unknown", example = "1")
    @JsonProperty("sort_order")
    private Integer sortOrder;

    @Schema(description = "unknown", example = "1")
    @JsonProperty("is_delete")
    private Boolean isDelete;

}
