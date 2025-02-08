package com.liyuyouguo.common.beans.dto.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class SaveAdminMemoDto {

    private Integer id;

    private String text;

    @JsonProperty("print_info")
    private String printInfo;

    @JsonProperty("express_value")
    private String expressValue;

    private String remark;

}
