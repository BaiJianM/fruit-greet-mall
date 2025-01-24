package com.liyuyouguo.common.beans;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 统一封装分页返回数据
 *
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {

    @Schema(description = "总条数")
    private Long total;

    @Schema(description = "每页显示条数")
    private Long size;

    @Schema(description = "当前页")
    private Long current;

    @Schema(description = "分页总页数")
    private Long pages;

    @Schema(description = "数据列表")
    private List<T> records;


    // 以下避免controller层自动将包装类型转换为String类型

    public long getTotal() {
        return this.total == null ? 0 : this.total;
    }

    public long getSize() {
        return this.size == null ? 0 : this.size;
    }

    public long getCurrent() {
        return this.current == null ? 0 : this.current;
    }

    public long getPages() {
        return this.pages == null ? 0 : this.pages;
    }

    public List<T> getRecords() {
        return this.records == null ? new ArrayList<>() : this.records;
    }
}
