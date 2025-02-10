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
public class ShipperUpdateDto {

    private Integer id;

    private Boolean status;

    private Integer sort;
}
