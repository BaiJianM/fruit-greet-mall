package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class GalleryDto {

    private Integer id;

    private String url;

    private Integer sortOrder;

    @JsonProperty("is_delete")
    private Boolean isDelete;

    private Integer goodsId;

}
