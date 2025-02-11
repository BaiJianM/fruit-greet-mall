package com.liyuyouguo.common.beans.dto.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class OrderQueryAdminDto {

    private int page = 1;

    private int size = 10;

    private String orderSn;

    private String consignee;

    @JsonProperty("logistic_code")
    private String logisticCode;

    private String status;

}
