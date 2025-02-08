package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

import java.util.List;

/**
 * @author baijianmin
 */
@Data
public class GoodsGalleryEditDto {

    private List<GoodsGalleryEditItem> data;

    @Data
    public static class GoodsGalleryEditItem {
        private Integer id;
        private Integer sortOrder;
    }
}
