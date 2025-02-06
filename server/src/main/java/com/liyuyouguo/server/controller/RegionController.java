package com.liyuyouguo.server.controller;

import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.dto.shop.RegionInfoQueryDto;
import com.liyuyouguo.common.beans.dto.shop.RegionQueryDto;
import com.liyuyouguo.common.beans.vo.RegionVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import com.liyuyouguo.common.entity.shop.Region;
import com.liyuyouguo.server.service.RegionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author baijianmin
 */
@Slf4j
@FruitGreetController("/region")
@RequiredArgsConstructor
@Validated
public class RegionController {

    private final RegionService regionService;

    @GetMapping("info")
    public FruitGreetResponse<Region> getRegion(@RequestParam Integer regionId) {
        return FruitGreetResponse.success(regionService.getRegion(regionId));
    }

    @GetMapping("list")
    public FruitGreetResponse<List<Region>> listRegion(@RequestParam Integer parentId) {
        return FruitGreetResponse.success(regionService.listRegion(parentId));
    }

    @PostMapping("data")
    public FruitGreetResponse<List<Region>> getRegionData(@RequestBody RegionQueryDto dto) {
        return FruitGreetResponse.success(regionService.getRegionData(dto));
    }

    @PostMapping("code")
    public FruitGreetResponse<RegionVo> getRegionInfo(@RequestBody RegionInfoQueryDto dto) {
        return FruitGreetResponse.success(regionService.getRegionInfo(dto));
    }

}
