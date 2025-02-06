package com.liyuyouguo.server.controller;

import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.vo.SearchIndexVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
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
@FruitGreetController("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/index")
    public FruitGreetResponse<SearchIndexVo> index() {
        return FruitGreetResponse.success(searchService.index());
    }

    @GetMapping("/helper")
    public FruitGreetResponse<List<String>> helper(@RequestParam String keyword) {
        return FruitGreetResponse.success(searchService.helper(keyword));
    }

    @PostMapping("/helper")
    public FruitGreetResponse<Void> clearHistory() {
        searchService.clearHistory();
        return FruitGreetResponse.success();
    }

}
