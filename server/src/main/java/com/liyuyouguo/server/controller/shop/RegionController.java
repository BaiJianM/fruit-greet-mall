package com.liyuyouguo.server.controller.shop;

import com.liyuyouguo.server.annotations.FruitShopController;
import com.liyuyouguo.server.beans.dto.shop.RegionInfoQueryDto;
import com.liyuyouguo.server.beans.dto.shop.RegionQueryDto;
import com.liyuyouguo.server.beans.vo.shop.RegionVo;
import com.liyuyouguo.server.commons.FruitShopResponse;
import com.liyuyouguo.server.entity.shop.Region;
import com.liyuyouguo.server.service.shop.RegionService;
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
@FruitShopController("/region")
@RequiredArgsConstructor
@Validated
public class RegionController {

    private final RegionService regionService;

    @GetMapping("info")
    public FruitShopResponse<Region> getRegion(@RequestParam Integer regionId) {
        return FruitShopResponse.success(regionService.getRegion(regionId));
    }

    @GetMapping("list")
    public FruitShopResponse<List<Region>> listRegion(@RequestParam Integer parentId) {
        return FruitShopResponse.success(regionService.listRegion(parentId));
    }

    @PostMapping("data")
    public FruitShopResponse<List<Region>> getRegionData(@RequestBody RegionQueryDto dto) {
        return FruitShopResponse.success(regionService.getRegionData(dto));
    }

    @PostMapping("code")
    public FruitShopResponse<RegionVo> getRegionInfo(@RequestBody RegionInfoQueryDto dto) {
        return FruitShopResponse.success(regionService.getRegionInfo(dto));
    }

}
