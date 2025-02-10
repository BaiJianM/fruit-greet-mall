package com.liyuyouguo.common.beans.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipperListDto {

    private Integer page = 1;

    private Integer size = 10;

    private String name;
}
