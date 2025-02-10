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

    @PostMapping("/destory")
    public FruitGreetResponse<Void> destory(@RequestBody ShipperDestoryDto dto) {
        shipperService.destory(dto);
        return FruitGreetResponse.success();
    }

    @GetMapping("/freight")
    public FruitGreetResponse<List<FreightTemplate>> freight() {
        return FruitGreetResponse.success(shipperService.freight());
    }

    @GetMapping("/getareadata")
    public FruitGreetResponse<List<Region>> getareadata() {
        return FruitGreetResponse.success(shipperService.getareadata());
    }

    @PostMapping("/freightDetail")
    public FruitGreetResponse<FreightDetailVo> freightDetail(@RequestBody FreightDetailDto dto) {
        return FruitGreetResponse.success(shipperService.getFreightDetail(dto));
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

    @GetMapping("/exceptarea")
    public FruitGreetResponse<List<ExceptArea>> exceptarea() {
        return FruitGreetResponse.success(shipperService.exceptarea());
    }

    @PostMapping("/exceptAreaDelete")
    public FruitGreetResponse<Void> exceptAreaDelete(@RequestBody ExceptAreaDeleteDto dto) {
        shipperService.exceptAreaDelete(dto.getId());
        return FruitGreetResponse.success();
    }

    @PostMapping("/exceptAreaDetail")
    public FruitGreetResponse<ExceptArea> exceptAreaDetail(@RequestBody ExceptAreaDeleteDto dto) {
        return FruitGreetResponse.success(shipperService.getExceptAreaDetail(dto.getId()));
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
