package com.liyuyouguo.common.beans.dto.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class CategorySaveDto {

    private Integer id;

    private String name;

    private Integer parentId;

    private Integer sortOrder;

    private Boolean isShow;

    private Boolean isChannel;

    private Boolean isCategory;

    private String frontName;

    private String iconUrl;

    private String imgUrl;

    @JsonProperty("pHeight")
    private Integer pHeight;

}
