package com.liyuyouguo.admin.controller;

import com.liyuyouguo.admin.service.IndexService;
import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.vo.DashboardInfoVo;
import com.liyuyouguo.common.beans.vo.DashboardVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 *
 * @author baijianmin
 */
@Slf4j
@FruitGreetController("/index")
@RequiredArgsConstructor
@Validated
public class IndexController {

    private final IndexService indexService;

    @GetMapping("/index")
    public FruitGreetResponse<DashboardVo> index() {
        return FruitGreetResponse.success(indexService.getDashboardInfo());
    }

    @GetMapping("/main")
    public FruitGreetResponse<DashboardInfoVo> mainDashboard(@RequestParam Integer pindex) {
        return FruitGreetResponse.success(indexService.getDashboardInfo(pindex));
    }

}
