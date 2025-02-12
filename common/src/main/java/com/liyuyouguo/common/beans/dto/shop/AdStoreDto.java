package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class AdStoreDto {

    private Integer id;

    private String imageUrl;

    private Integer linkType;

    private Boolean enabled;

    private String endTime;

    private Integer goodsId;

    private String link;

}
