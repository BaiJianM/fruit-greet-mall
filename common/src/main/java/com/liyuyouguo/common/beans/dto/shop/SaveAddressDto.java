package com.liyuyouguo.common.beans.dto.shop;

import com.liyuyouguo.common.entity.shop.Address;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author baijianmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SaveAddressDto extends Address {

    private List<Integer> addOptions;

}
