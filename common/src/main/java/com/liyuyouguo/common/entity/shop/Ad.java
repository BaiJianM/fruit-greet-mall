package com.liyuyouguo.common.entity.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class Ad {

    /**
     * 主键
     */
    @Schema(description = "主键", example = "1")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 0-商品，1-链接
     */
    @Schema(description = "链接类型", example = "0")
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
    private Integer goodsId;

    @Schema(description = "unknown", example = "1")
    private String imageUrl;

    @Schema(description = "unknown", example = "1")
    private LocalDateTime endTime;

    @Schema(description = "unknown", example = "1")
    private Boolean enabled;

    @Schema(description = "unknown", example = "1")
    private Integer sortOrder;

    @Schema(description = "unknown", example = "1")
    private Boolean isDelete;

}
