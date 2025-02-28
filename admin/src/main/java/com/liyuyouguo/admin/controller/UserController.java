package com.liyuyouguo.admin.controller;

import com.liyuyouguo.admin.service.AdminUserService;
import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.beans.dto.shop.SaveAddressDto;
import com.liyuyouguo.common.beans.dto.shop.UpdateUserInfoDto;
import com.liyuyouguo.common.beans.dto.shop.UserStoreDto;
import com.liyuyouguo.common.beans.vo.OrderAdminVo;
import com.liyuyouguo.common.beans.vo.UserAddressVo;
import com.liyuyouguo.common.beans.vo.UserDataInfoVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import com.liyuyouguo.common.entity.shop.Cart;
import com.liyuyouguo.common.entity.shop.Footprint;
import com.liyuyouguo.common.entity.shop.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author baijianmin
 */
@Slf4j
@FruitGreetController("/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public FruitGreetResponse<PageResult<User>> index(@RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "") String nickname) {
        return FruitGreetResponse.success(adminUserService.getUserList(page, size, nickname));
    }

    @GetMapping("/info")
    public FruitGreetResponse<User> info(@RequestParam Integer id) {
        return FruitGreetResponse.success(adminUserService.getUserInfo(id));
    }

    @GetMapping("/dataInfo")
    public FruitGreetResponse<UserDataInfoVo> getUserDataInfo(@RequestParam Integer id) {
        return FruitGreetResponse.success(adminUserService.getUserDataInfo(id));
    }

    @GetMapping("/address")
    public FruitGreetResponse<PageResult<UserAddressVo>> getAddressList(@RequestParam("id") Integer id,
                                                                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return FruitGreetResponse.success(adminUserService.getAddressList(id, page, size));
    }

    @PostMapping("/saveAddress")
    public FruitGreetResponse<Void> saveAddress(@RequestBody SaveAddressDto dto) {
        adminUserService.saveUserAddress(dto);
        return FruitGreetResponse.success();
    }

    @GetMapping("/cartData")
    public FruitGreetResponse<PageResult<Cart>> getCartData(@RequestParam("id") Integer id,
                                                            @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return FruitGreetResponse.success(adminUserService.getCartData(id, page, size));
    }

    @GetMapping("/foot")
    public FruitGreetResponse<PageResult<Footprint>> foot(@RequestParam Integer id,
                                                          @RequestParam(defaultValue = "1") Integer page,
                                                          @RequestParam(defaultValue = "10") Integer size) {
        return FruitGreetResponse.success(adminUserService.getFootprints(id, page, size));
    }

    @GetMapping("/order")
    public FruitGreetResponse<PageResult<OrderAdminVo>> order(@RequestParam("id") Integer userId,
                                                              @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return FruitGreetResponse.success(adminUserService.getOrderList(userId, page, size));
    }

    @PostMapping("/updateInfo")
    public FruitGreetResponse<Void> updateInfo(@RequestBody UpdateUserInfoDto dto) {
        adminUserService.updateUserInfo(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/updateName")
    public FruitGreetResponse<Void> updateName(@RequestBody UpdateUserInfoDto dto) {
        adminUserService.updateName(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/updateMobile")
    public FruitGreetResponse<Void> updateMobile(@RequestBody UpdateUserInfoDto dto) {
        adminUserService.updateMobile(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/store")
    public FruitGreetResponse<Void> store(@RequestBody UserStoreDto dto) {
        adminUserService.store(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/destroy")
    public FruitGreetResponse<Void> destroy(@RequestParam Integer id) {
        adminUserService.destroy(id);
        return FruitGreetResponse.success();
    }

}
