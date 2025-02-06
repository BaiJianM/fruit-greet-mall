package com.liyuyouguo.server.controller;

import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.dto.shop.AddressSaveDto;
import com.liyuyouguo.common.beans.vo.AddressVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
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
@FruitGreetController("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * 获取收货地址
     *
     * @return List<AddressVo> 收货地址信息
     */
    @GetMapping("/getAddresses")
    public FruitGreetResponse<List<AddressVo>> getAddresses() {
        return FruitGreetResponse.success(addressService.getAddresses());
    }

    /**
     * 保存收货地址
     *
     * @param dto 收货地址信息传参
     * @return Address 收货地址信息
     */
    @PostMapping("/saveAddress")
    public FruitGreetResponse<Address> saveAddress(@RequestBody AddressSaveDto dto) {
        return FruitGreetResponse.success(addressService.saveAddress(dto));
    }

    /**
     * 删除收货地址
     *
     * @param addressId 收货地址id
     * @return Integer 影响行数
     */
    @PostMapping("/deleteAddress")
    public FruitGreetResponse<Integer> deleteAddress(@RequestParam("id") Integer addressId) {
        return FruitGreetResponse.success(addressService.deleteAddress(addressId));
    }

    /**
     * 获取收货地址详情
     *
     * @param addressId 收货地址id
     * @return AddressVo 收货地址详情信息
     */
    @GetMapping("/addressDetail")
    public FruitGreetResponse<AddressVo> addressDetail(@RequestParam("id") Integer addressId) {
        return FruitGreetResponse.success(addressService.addressDetail(addressId));
    }

}
