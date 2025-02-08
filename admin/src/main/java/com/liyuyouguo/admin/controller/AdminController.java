package com.liyuyouguo.admin.controller;

import com.liyuyouguo.admin.service.AdminService;
import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.dto.shop.AdminAddDto;
import com.liyuyouguo.common.beans.dto.shop.AdminSaveDto;
import com.liyuyouguo.common.beans.dto.shop.SettingsDto;
import com.liyuyouguo.common.beans.dto.shop.ShowSettingsDto;
import com.liyuyouguo.common.beans.vo.AdminVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import com.liyuyouguo.common.entity.shop.Admin;
import com.liyuyouguo.common.entity.shop.Settings;
import com.liyuyouguo.common.entity.shop.ShowSettings;
import com.liyuyouguo.common.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 管理控制层
 *
 * @author baijianmin
 */
@Slf4j
@FruitGreetController("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final AdminService adminService;

    @GetMapping("index")
    public FruitGreetResponse<List<AdminVo>> index() {
        return FruitGreetResponse.success(adminService.index());
    }

    @PostMapping("/adminDetail")
    public FruitGreetResponse<Admin> adminDetail(@RequestParam Long id) {
        return FruitGreetResponse.success(adminService.adminDetail(id));
    }

    @PostMapping("/adminAdd")
    public FruitGreetResponse<Void> adminAdd(@RequestBody AdminAddDto adminAddDto) {
        adminService.adminAdd(adminAddDto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/adminSave")
    public FruitGreetResponse<Void> adminSave(@RequestBody Map<String, Object> requestBody) {
        // 获取 user 数据
        AdminSaveDto adminSaveDto = ConvertUtils.convert(requestBody.get("user"), AdminSaveDto::new)
                .orElseThrow(() -> new IllegalArgumentException("参数错误"));
        // 获取 change 变量
        boolean change = Boolean.parseBoolean(requestBody.get("change").toString());
        adminService.adminSave(adminSaveDto, change);
        return FruitGreetResponse.success();
    }

    @GetMapping("/info")
    public FruitGreetResponse<Admin> info(@RequestParam Long id) {
        return FruitGreetResponse.success(adminService.info(id));
    }

    @PostMapping("/deleAdmin")
    public FruitGreetResponse<Void> deleteAdmin(@RequestBody Map<String, Long> requestBody) {
        Long id = requestBody.get("id");
        adminService.deleteAdmin(id);
        return FruitGreetResponse.success();
    }

    @GetMapping("/showset")
    public FruitGreetResponse<ShowSettings> showSet() {
        return FruitGreetResponse.success(adminService.showSet());
    }

    @PostMapping("/showsetStore")
    public FruitGreetResponse<ShowSettings> showSetStore(@RequestBody ShowSettingsDto showSettingsDto) {
        return FruitGreetResponse.success(adminService.showSetStore(showSettingsDto));
    }

    @PostMapping("/changeAutoStatus")
    public FruitGreetResponse<Void> changeAutoStatus(@RequestBody Map<String, Boolean> requestBody) {
        Boolean status = requestBody.get("status");
        adminService.changeAutoStatus(status);
        return FruitGreetResponse.success();
    }

    @PostMapping("/storeShipperSettings")
    public FruitGreetResponse<Void> storeShipperSettings(@RequestBody SettingsDto SettingsDto) {
        adminService.storeShipperSettings(SettingsDto);
        return FruitGreetResponse.success();
    }

    @GetMapping("/senderInfo")
    public FruitGreetResponse<Settings> senderInfo() {
        return FruitGreetResponse.success(adminService.senderInfo());
    }

}
