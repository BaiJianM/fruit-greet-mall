package com.liyuyouguo.admin.controller;

import com.liyuyouguo.admin.service.WapService;
import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.beans.dto.shop.UpdatePriceDto;
import com.liyuyouguo.common.beans.vo.CategoryOptionVo;
import com.liyuyouguo.common.beans.vo.GoodsInfoAdminVo;
import com.liyuyouguo.common.beans.vo.OutOfStockGoodsVo;
import com.liyuyouguo.common.beans.vo.WapIndexVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
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
 * @author baijianmin
 */
@Slf4j
@FruitGreetController("/wap")
@RequiredArgsConstructor
@Validated
public class WapController {

    private final WapService wapService;

    @GetMapping
    public FruitGreetResponse<List<WapIndexVo>> index() {
        return FruitGreetResponse.success(wapService.getProductList());
    }

    @GetMapping("/onSale")
    public FruitGreetResponse<List<WapIndexVo>> onSale() {
        return FruitGreetResponse.success(wapService.getOnSaleList());
    }

    @GetMapping("/outSale")
    public FruitGreetResponse<List<WapIndexVo>> outSale() {
        return FruitGreetResponse.success(wapService.getOutSaleList());
    }

    @PostMapping("/updatePrice")
    public FruitGreetResponse<Void> updatePrice(@RequestBody UpdatePriceDto dto) {
        wapService.updatePrice(dto);
        return FruitGreetResponse.success();
    }

    @GetMapping("/out")
    public FruitGreetResponse<PageResult<OutOfStockGoodsVo>> out(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return FruitGreetResponse.success(wapService.getOutOfStockGoods(page, size));
    }

    @GetMapping("/drop")
    public FruitGreetResponse<PageResult<OutOfStockGoodsVo>> drop(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return FruitGreetResponse.success(wapService.getDropGoods(page, size));
    }

    @GetMapping("/sort")
    public FruitGreetResponse<PageResult<OutOfStockGoodsVo>> sort(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                  @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                  @RequestParam("index") Integer index) {
        return FruitGreetResponse.success(wapService.sort(page, size, index));
    }

    @GetMapping("/saleStatus")
    public FruitGreetResponse<Void> saleStatus(@RequestParam("id") Integer id, @RequestParam("status") String status) {
        wapService.updateSaleStatus(id, status);
        return FruitGreetResponse.success();
    }

    @GetMapping("/info")
    public FruitGreetResponse<GoodsInfoAdminVo> infoAction(@RequestParam("id") Integer id) {
        return FruitGreetResponse.success(wapService.getGoodsInfo(id));
    }

    @GetMapping("/getAllCategory")
    public FruitGreetResponse<List<CategoryOptionVo>> getAllCategory() {
        return FruitGreetResponse.success(wapService.getAllCategory());
    }

    // TODO 接口可能没用到
    @GetMapping("/getGoodsSnName")
    public FruitGreetResponse<List<Goods>> getGoodsSnNameAction(@RequestParam("cateId") Integer cateId) {
        return FruitGreetResponse.success(wapService.getGoodsSnName(cateId));
    }

    // TODO 接口可能没用到
//    @PostMapping("/store")
//    public FruitGreetResponse<Void> store(@RequestBody GoodsStoreDto dto) {
//        wapService.store(dto);
//        return FruitGreetResponse.success();
//    }

}
