package com.liyuyouguo.admin.controller;

import com.liyuyouguo.admin.service.ShipperService;
import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import com.liyuyouguo.common.entity.shop.Shipper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/delivery")
    public FruitGreetResponse<List<Shipper>> delivery() {
        return FruitGreetResponse.success(shipperService.getEnabledShippers());
    }

}
