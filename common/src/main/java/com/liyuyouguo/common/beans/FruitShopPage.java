package com.liyuyouguo.common.beans;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分页查询基础类
 *
 * @author baijianmin
 */
@Data
public class FruitShopPage {

    @Schema(description = "当前页", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long current;

    @Schema(description = "每页大小", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long size;

    public FruitShopPage() {
        this.current = 1L;
        this.size = 10L;
    }
}
