package com.liyuyouguo.server.controller.shop;

import com.liyuyouguo.common.annotations.FruitShopController;
import com.liyuyouguo.common.beans.dto.shop.AddressSaveDto;
import com.liyuyouguo.common.beans.vo.shop.AddressVo;
import com.liyuyouguo.common.commons.FruitShopResponse;
import com.liyuyouguo.common.entity.shop.Address;
import com.liyuyouguo.server.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author baijianmin
 */
@Slf4j
@FruitShopController("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * 获取收货地址
     *
     * @return List<AddressVo> 收货地址信息
     */
    @GetMapping("/getAddresses")
    public FruitShopResponse<List<AddressVo>> getAddresses() {
        return FruitShopResponse.success(addressService.getAddresses());
    }

    /**
     * 保存收货地址
     *
     * @param dto 收货地址信息传参
     * @return Address 收货地址信息
     */
    @PostMapping("/saveAddress")
    public FruitShopResponse<Address> saveAddress(@RequestBody AddressSaveDto dto) {
        return FruitShopResponse.success(addressService.saveAddress(dto));
    }

    /**
     * 删除收货地址
     *
     * @param addressId 收货地址id
     * @return Integer 影响行数
     */
    @PostMapping("/deleteAddress")
    public FruitShopResponse<Integer> deleteAddress(@RequestParam("id") Integer addressId) {
        return FruitShopResponse.success(addressService.deleteAddress(addressId));
    }

    /**
     * 获取收货地址详情
     *
     * @param addressId 收货地址id
     * @return AddressVo 收货地址详情信息
     */
    @GetMapping("/addressDetail")
    public FruitShopResponse<AddressVo> addressDetail(@RequestParam("id") Integer addressId) {
        return FruitShopResponse.success(addressService.addressDetail(addressId));
    }

}
