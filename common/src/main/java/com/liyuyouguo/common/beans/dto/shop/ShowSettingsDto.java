package com.liyuyouguo.common.beans.dto.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class ShowSettingsDto {

    private Integer id;

    private Integer banner;

    private Integer channel;

    @JsonProperty("index_banner_img")
    private Integer indexBannerImg;

    private Integer notice;
}
