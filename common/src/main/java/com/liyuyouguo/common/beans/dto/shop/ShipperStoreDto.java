package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class ShipperStoreDto {

    private Integer id;

    private String name;

    private String code;

    private Integer sortOrder;

    private String monthCode;

    private String customerName;

    private Boolean enabled;

}
