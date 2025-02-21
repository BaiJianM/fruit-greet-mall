package com.liyuyouguo.common.beans.dto.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author baijianmin
 */
@Data
public class OrderSaveAddressDto {

    private Integer[] addOptions;

    private String address;

    private String avatar;

    private String cAddress;

    private String mobile;

    private String name;

    private String nickName;

    @JsonProperty("order_sn")
    private String orderSn;

    private String postscript;

}
