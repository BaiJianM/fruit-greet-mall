package com.liyuyouguo.admin.controller;

import com.liyuyouguo.admin.service.AdService;
import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.FruitGreetPage;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.beans.dto.shop.AdStoreDto;
import com.liyuyouguo.common.beans.dto.shop.AdUpdateSortDto;
import com.liyuyouguo.common.beans.vo.AdVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import com.liyuyouguo.common.entity.shop.Ad;
import com.liyuyouguo.common.entity.shop.Goods;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 广告控制层
 *
 * @author baijianmin
 */
@Slf4j
@FruitGreetController("/ad")
@RequiredArgsConstructor
@Validated
public class AdController {

    private final AdService adService;

    @GetMapping
    public FruitGreetResponse<PageResult<AdVo>> index(FruitGreetPage page) {
        return FruitGreetResponse.success(adService.index(page));
    }

    @PostMapping("/updateSort")
    public FruitGreetResponse<Integer> updateSort(@RequestBody AdUpdateSortDto dto) {
        return FruitGreetResponse.success(adService.updateSort(dto));
    }

    @GetMapping("/info")
    public FruitGreetResponse<Ad> info(@RequestParam("id") Integer adId) {
        return FruitGreetResponse.success(adService.info(adId));
    }

    @PostMapping("/store")
    public FruitGreetResponse<AdVo> store(@RequestBody AdStoreDto dto) {
        return FruitGreetResponse.success(adService.store(dto));
    }

    @GetMapping("/getAllRelate")
    public FruitGreetResponse<List<Goods>> getAllRelate() {
        return FruitGreetResponse.success(adService.getAllRelate());
    }

    @PostMapping("/destroy")
    public FruitGreetResponse<Void> destroy(@RequestParam Integer id) {
        adService.destroy(id);
        return FruitGreetResponse.success();
    }

    @GetMapping("/saleStatus")
    public FruitGreetResponse<Void> saleStatus(@RequestParam("id") Integer adId, @RequestParam String status) {
        adService.saleStatus(adId, status);
        return FruitGreetResponse.success();
    }

}
