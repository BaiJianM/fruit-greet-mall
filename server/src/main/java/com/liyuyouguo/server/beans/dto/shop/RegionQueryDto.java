package com.liyuyouguo.server.beans.dto.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class RegionQueryDto {

    @JsonProperty("parent_id")
    private Integer parentId;

}
