package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class GalleryVo {

    private Integer id;

    private String url;

    @JsonProperty("is_delete")
    private Integer isDelete;

}
