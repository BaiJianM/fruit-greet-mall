package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

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

    private String orderSn;

    private String postscript;

}
