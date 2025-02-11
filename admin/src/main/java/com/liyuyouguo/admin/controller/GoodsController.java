package com.liyuyouguo.admin.controller;

import com.liyuyouguo.admin.service.GoodsService;
import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.beans.dto.shop.*;
import com.liyuyouguo.common.beans.dto.shop.GoodsStoreDto;
import com.liyuyouguo.common.beans.vo.*;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import com.liyuyouguo.common.entity.shop.Goods;
import com.liyuyouguo.common.entity.shop.GoodsGallery;
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
@FruitGreetController("/goods")
@RequiredArgsConstructor
@Validated
public class GoodsController {

    private final GoodsService goodsService;

    @GetMapping
    public FruitGreetResponse<PageResult<GoodsVo>> index(@RequestParam(value = "page", defaultValue = "1") int page,
                                                         @RequestParam(value = "size", defaultValue = "10") int size,
                                                         @RequestParam(value = "name", defaultValue = "") String name) {
        return FruitGreetResponse.success(goodsService.getGoodsWithCategoryAndProduct(page, size, name));
    }

    @GetMapping("/getExpressData")
    public FruitGreetResponse<ExpressDataVo> getExpressData() {
        return FruitGreetResponse.success(goodsService.getExpressData());
    }

    @PostMapping("/copygoods")
    public FruitGreetResponse<Integer> copyGoods(@RequestBody GoodsCopyDto goodsCopyDto) {
        return FruitGreetResponse.success(goodsService.copyGoods(goodsCopyDto.getId()));
    }

    @PostMapping("/updateStock")
    public FruitGreetResponse<Void> updateStock(@RequestParam String goodsSn, @RequestParam Integer goodsNumber) {
        goodsService.updateStock(goodsSn, goodsNumber);
        return FruitGreetResponse.success();
    }

    @PostMapping("/updateGoodsNumber")
    public FruitGreetResponse<Void> updateGoodsNumber() {
        goodsService.updateGoodsNumber();
        return FruitGreetResponse.success();
    }

    @GetMapping("/onsaleAction")
    public FruitGreetResponse<PageResult<GoodsVo>> onSaleAction(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                @RequestParam(value = "size", defaultValue = "10") int size) {
        return FruitGreetResponse.success(goodsService.getOnSaleGoods(page, size));
    }

    @GetMapping("/out")
    public FruitGreetResponse<PageResult<GoodsVo>> out(@RequestParam(value = "page", defaultValue = "1") int page,
                                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        return FruitGreetResponse.success(goodsService.out(page, size));
    }

    @GetMapping("/drop")
    public FruitGreetResponse<PageResult<GoodsVo>> drop(@RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        return FruitGreetResponse.success(goodsService.drop(page, size));
    }

    @GetMapping("/sort")
    public FruitGreetResponse<PageResult<GoodsVo>> sort(@RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size,
                                                        @RequestParam(value = "index") int index) {
        return FruitGreetResponse.success(goodsService.sort(page, size, index));
    }

    @PostMapping("/saleStatus")
    public FruitGreetResponse<Void> saleStatus(@RequestParam("id") Integer id,
                                               @RequestParam("status") String status) {
        goodsService.updateSaleStatus(id, status);
        return FruitGreetResponse.success();
    }

    @PostMapping("/productStatus")
    public FruitGreetResponse<Void> productStatus(@RequestParam("id") Integer id,
                                                  @RequestParam("status") Integer status) {
        goodsService.updateProductStatus(id, status);
        return FruitGreetResponse.success();
    }

    @PostMapping("/indexShowStatus")
    public FruitGreetResponse<Void> indexShowStatus(@RequestParam("id") Integer id,
                                                    @RequestParam("status") String status) {
        goodsService.updateIndexShowStatus(id, status);
        return FruitGreetResponse.success();
    }

    @GetMapping("/info")
    public FruitGreetResponse<GoodsInfoVo> getGoodsInfo(@RequestParam("id") Integer id) {
        return FruitGreetResponse.success(goodsService.getGoodsInfo(id));
    }

    @GetMapping("/getAllSpecification")
    public FruitGreetResponse<List<SpecificationOptionVo>> getAllSpecification() {
        return FruitGreetResponse.success(goodsService.getAllSpecifications());
    }

    @GetMapping("/getAllCategory")
    public FruitGreetResponse<List<CategoryOptionVo>> getAllCategory() {
        return FruitGreetResponse.success(goodsService.getAllCategory());
    }

    @PostMapping("/store")
    public FruitGreetResponse<Integer> storeAction(@RequestBody GoodsStoreDto dto) {
        return FruitGreetResponse.success(goodsService.store(dto));
    }

    @PostMapping("/updatePrice")
    public FruitGreetResponse<Void> updatePrice(@RequestBody GoodsUpdatePriceDto dto) {
        goodsService.updatePrice(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/checkSku")
    public FruitGreetResponse<Void> checkSku(@RequestBody CheckSkuDto dto) {
        goodsService.checkSku(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/updateSort")
    public FruitGreetResponse<Integer> updateSort(@RequestBody CategoryUpdateSortDto dto) {
        return FruitGreetResponse.success(goodsService.updateSort(dto));
    }

//    @PostMapping("/updateShortName")
//    public FruitGreetResponse<Integer> updateShortName(@RequestBody GoodsUpdateShortNameDto dto) {
//        return FruitGreetResponse.success(goodsService.updateShortName(dto));
//    }

    @GetMapping("/galleryList")
    public FruitGreetResponse<List<GoodsGallery>> galleryList(@RequestParam Long id) {
        return FruitGreetResponse.success(goodsService.getGalleryList(id));
    }

    @PostMapping("/gallery")
    public FruitGreetResponse<Void> addGallery(@RequestBody GoodsGalleryDto dto) {
        goodsService.addGallery(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("getGalleryList")
    public FruitGreetResponse<GalleryListVo> getGalleryList(@RequestBody GoodsGalleryListDto dto) {
        return FruitGreetResponse.success(goodsService.getGalleryList(dto));
    }

    @PostMapping("/deleteGalleryFile")
    public FruitGreetResponse<String> deleteGalleryFile(@RequestBody GoodsGalleryDeleteDto dto) {
        goodsService.deleteGalleryFile(dto);
        return FruitGreetResponse.success("文件删除成功");
    }

    @PostMapping("/galleryEdit")
    public FruitGreetResponse<Void> galleryEdit(@RequestBody GoodsGalleryEditDto dto) {
        goodsService.galleryEdit(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/deleteListPicUrl")
    public FruitGreetResponse<Void> deleteListPicUrl(@RequestBody GoodsDeleteListPicDto dto) {
        goodsService.deleteListPicUrl(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/destroy")
    public FruitGreetResponse<Void> destroy(@RequestBody GoodsDestroyDto dto) {
        goodsService.destroy(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/uploadHttpsImage")
    public FruitGreetResponse<String> uploadHttpsImage(@RequestBody GoodsUploadImageDto dto) {
        String imageUrl = goodsService.uploadHttpsImage(dto);
        return FruitGreetResponse.success(imageUrl);
    }

}
