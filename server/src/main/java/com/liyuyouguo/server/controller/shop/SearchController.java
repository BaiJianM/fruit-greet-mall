package com.liyuyouguo.server.controller.shop;

import com.liyuyouguo.common.annotations.FruitShopController;
import com.liyuyouguo.common.beans.vo.shop.SearchIndexVo;
import com.liyuyouguo.common.commons.FruitShopResponse;
import com.liyuyouguo.server.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author baijianmin
 */
@Slf4j
@FruitShopController("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/index")
    public FruitShopResponse<SearchIndexVo> index() {
        return FruitShopResponse.success(searchService.index());
    }

    @GetMapping("/helper")
    public FruitShopResponse<List<String>> helper(@RequestParam String keyword) {
        return FruitShopResponse.success(searchService.helper(keyword));
    }

    @PostMapping("/helper")
    public FruitShopResponse<Void> clearHistory() {
        searchService.clearHistory();
        return FruitShopResponse.success();
    }

}
