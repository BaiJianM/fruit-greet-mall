package com.liyuyouguo.common.beans.dto.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class UpdateSpecificationDto {

    private Integer id;

    private String name;

    @JsonProperty("sort_order")
    private Integer sortOrder;

}
