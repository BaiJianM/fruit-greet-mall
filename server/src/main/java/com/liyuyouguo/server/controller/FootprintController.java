package com.liyuyouguo.server.controller;

import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.FruitGreetPage;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import com.liyuyouguo.common.entity.shop.Footprint;
import com.liyuyouguo.server.service.FootPrintService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author baijianmin
 */
@Slf4j
@FruitGreetController("/footprint")
@RequiredArgsConstructor
public class FootprintController {

    private final FootPrintService footPrintService;

    /**
     * 删除当天的同一个商品的足迹
     *
     * @param footprintId 足迹id
     * @return String 删除结果
     */
    @PostMapping("/delete")
    public FruitGreetResponse<String> delete(@RequestParam("footprintId") Integer footprintId) {
        return FruitGreetResponse.success(footPrintService.delete(footprintId));
    }

    /**
     * 获取用户足迹
     *
     * @param pageDto 分页参数
     * @return PageResult<Footprint> 用户足迹
     */
    @PostMapping("/list")
    public FruitGreetResponse<PageResult<Footprint>> getFootprintList(@RequestBody FruitGreetPage pageDto) {
        return FruitGreetResponse.success(footPrintService.getFootprintList(pageDto));
    }

}
