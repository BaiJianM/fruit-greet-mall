package com.liyuyouguo.common.beans.vo;

import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class GalleryDto {

    private Integer id;

    private String url;

    private Integer sortOrder;

    private Boolean isDelete;

    private Integer goodsId;

}
