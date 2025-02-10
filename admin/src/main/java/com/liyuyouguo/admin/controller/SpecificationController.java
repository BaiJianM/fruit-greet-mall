package com.liyuyouguo.admin.controller;

import com.liyuyouguo.admin.service.SpecificationService;
import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.dto.shop.*;
import com.liyuyouguo.common.beans.vo.GoodsSpecVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import com.liyuyouguo.common.entity.shop.Specification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author baijianmin
 */
@Slf4j
@FruitGreetController("/specification")
@RequiredArgsConstructor
@Validated
public class SpecificationController {

    private final SpecificationService specificationService;

    @GetMapping
    public FruitGreetResponse<List<Specification>> index() {
        return FruitGreetResponse.success(specificationService.getAllSpecifications());
    }

    @PostMapping("/getGoodsSpec")
    public FruitGreetResponse<GoodsSpecVo> getGoodsSpec(@RequestBody GoodsSpecDto dto) {
        return FruitGreetResponse.success(specificationService.getGoodsSpec(dto));
    }

    // TODO 可能没用上这个接口
    @PostMapping("/productUpdate")
    public FruitGreetResponse<Void> productUpdate(@RequestBody ProductUpdateDto dto) {
        specificationService.productUpdate(dto);
        return FruitGreetResponse.success();
    }

    // TODO 可能没用上这个接口
    @PostMapping("/productDele")
    public FruitGreetResponse<Void> productDele(@RequestBody ProductDeleDto dto) {
        specificationService.productDele(dto);
        return FruitGreetResponse.success();
    }

    // TODO 可能没用上这个接口
    @PostMapping("/delePrimarySpec")
    public FruitGreetResponse<Void> delePrimarySpec(@RequestBody ProductDeleSpecDto dto) {
        specificationService.delePrimarySpec(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/detail")
    public FruitGreetResponse<Specification> detail(@RequestBody SpecificationDetailDto dto) {
        return FruitGreetResponse.success(specificationService.getDetail(dto));
    }

    @PostMapping("/add")
    public FruitGreetResponse<Integer> add(@RequestBody SpecificationAddDto dto) {
        return FruitGreetResponse.success(specificationService.addSpecification(dto));
    }

    @PostMapping("/checkSn")
    public FruitGreetResponse<Void> checkSn(@RequestBody CheckSnDto dto) {
        specificationService.checkSn(dto.getSn());
        return FruitGreetResponse.success();
    }

    @PostMapping("/update")
    public FruitGreetResponse<Void> update(@RequestBody UpdateSpecificationDto dto) {
        specificationService.update(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/delete")
    public FruitGreetResponse<Void> delete(@RequestBody DeleteSpecificationDto dto) {
        specificationService.delete(dto);
        return FruitGreetResponse.success();
    }

}
