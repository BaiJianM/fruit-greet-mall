package com.liyuyouguo.admin.controller;

import com.liyuyouguo.admin.service.ShipperService;
import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.beans.dto.shop.*;
import com.liyuyouguo.common.beans.vo.FreightDetailVo;
import com.liyuyouguo.common.beans.vo.FreightSaveTableDto;
import com.liyuyouguo.common.beans.vo.ShipperIndexVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import com.liyuyouguo.common.entity.shop.ExceptArea;
import com.liyuyouguo.common.entity.shop.FreightTemplate;
import com.liyuyouguo.common.entity.shop.Region;
import com.liyuyouguo.common.entity.shop.Shipper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *
 *
 * @author baijianmin
 */
@Slf4j
@FruitGreetController("/shipper")
@RequiredArgsConstructor
@Validated
public class ShipperController {

    private final ShipperService shipperService;

    @GetMapping
    public FruitGreetResponse<ShipperIndexVo> index() {
        return FruitGreetResponse.success(shipperService.getShipperIndexData());
    }

    @GetMapping("/delivery")
    public FruitGreetResponse<List<Shipper>> delivery() {
        return FruitGreetResponse.success(shipperService.getEnabledShippers());
    }

    @GetMapping("/list")
    public FruitGreetResponse<PageResult<Shipper>> list(ShipperListDto dto) {
        return FruitGreetResponse.success(shipperService.getShipperList(dto));
    }

    @PostMapping("/enabledStatus")
    public FruitGreetResponse<Void> updateStatus(@RequestBody ShipperUpdateDto dto) {
        shipperService.updateStatus(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/updateSort")
    public FruitGreetResponse<Void> updateSort(@RequestBody ShipperUpdateDto dto) {
        shipperService.updateSort(dto);
        return FruitGreetResponse.success();
    }

    @GetMapping("/info")
    public FruitGreetResponse<Shipper> info(@RequestParam Integer id) {
        return FruitGreetResponse.success(shipperService.getShipperInfo(id));
    }

    @PostMapping("/store")
    public FruitGreetResponse<Void> store(@RequestBody ShipperStoreDto dto) {
        shipperService.store(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/destroy")
    public FruitGreetResponse<Void> destroy(@RequestParam Integer id) {
        shipperService.destroy(id);
        return FruitGreetResponse.success();
    }

    @GetMapping("/freight")
    public FruitGreetResponse<List<FreightTemplate>> freight() {
        return FruitGreetResponse.success(shipperService.freight());
    }

    @GetMapping("/getAreaData")
    public FruitGreetResponse<List<Region>> getAreaData() {
        return FruitGreetResponse.success(shipperService.getAreaData());
    }

    @PostMapping("/freightDetail")
    public FruitGreetResponse<FreightDetailVo> freightDetail(@RequestParam Integer id) {
        return FruitGreetResponse.success(shipperService.getFreightDetail(id));
    }

    @PostMapping("/saveTable")
    public FruitGreetResponse<Void> saveTable(@RequestBody FreightSaveTableDto dto) {
        shipperService.saveTable(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/addTable")
    public FruitGreetResponse<Void> addTable(@RequestBody FreightAddTableDto dto) {
        shipperService.addTable(dto);
        return FruitGreetResponse.success();
    }

    @GetMapping("/exceptArea")
    public FruitGreetResponse<List<ExceptArea>> exceptarea() {
        return FruitGreetResponse.success(shipperService.exceptarea());
    }

    @PostMapping("/exceptAreaDelete")
    public FruitGreetResponse<Void> exceptAreaDelete(@RequestParam Integer id) {
        shipperService.exceptAreaDelete(id);
        return FruitGreetResponse.success();
    }

    @PostMapping("/exceptAreaDetail")
    public FruitGreetResponse<ExceptArea> exceptAreaDetail(@RequestParam Integer id) {
        return FruitGreetResponse.success(shipperService.getExceptAreaDetail(id));
    }

    @PostMapping("/saveExceptArea")
    public FruitGreetResponse<Void> saveExceptArea(@RequestBody SaveExceptAreaDto dto) {
        shipperService.saveExceptArea(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/addExceptArea")
    public FruitGreetResponse<Void> addExceptArea(@RequestBody AddExceptAreaDto dto) {
        shipperService.addExceptArea(dto);
        return FruitGreetResponse.success();
    }

}
