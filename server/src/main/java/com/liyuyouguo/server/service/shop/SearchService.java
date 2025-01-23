package com.liyuyouguo.server.service.shop;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liyuyouguo.server.beans.vo.shop.SearchIndexVo;
import com.liyuyouguo.server.entity.shop.Keywords;
import com.liyuyouguo.server.entity.shop.SearchHistory;
import com.liyuyouguo.server.mapper.KeywordsMapper;
import com.liyuyouguo.server.mapper.SearchHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final KeywordsMapper keywordsMapper;

    private final SearchHistoryMapper searchHistoryMapper;

    public SearchIndexVo index() {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        List<Keywords> keywordList = keywordsMapper.selectList(null);
        // 取出输入框默认的关键词
        Keywords defaultKeyword = keywordList.stream().filter(k -> k.getIsDefault() == 1).findFirst().orElseThrow();
        // 取出热门关键词
        List<Keywords> hotKeywordList = keywordList.stream().collect(Collectors.toMap(Keywords::getKeyword, Function.identity(), (t, t1) -> t))
                .values().stream().skip(10).toList();
        List<SearchHistory> searchHistories = searchHistoryMapper.selectList(Wrappers.lambdaQuery(SearchHistory.class)
                .eq(SearchHistory::getUserId, userId));
        List<String> historyKeywordList = searchHistories.stream().collect(Collectors.toMap(SearchHistory::getKeyword, SearchHistory::getKeyword, (t, t1) -> t))
                .values().stream().skip(10).toList();
        SearchIndexVo searchIndexVo = new SearchIndexVo();
        searchIndexVo.setDefaultKeyword(defaultKeyword);
        searchIndexVo.setHotKeywordList(hotKeywordList);
        searchIndexVo.setHistoryKeywordList(historyKeywordList);
        return searchIndexVo;
    }

    public List<String> helper(String keyword) {
        List<Keywords> keywords = keywordsMapper.selectList(Wrappers.lambdaQuery(Keywords.class)
                .likeRight(Keywords::getKeyword, keyword));
        return keywords.stream().collect(Collectors.toMap(Keywords::getKeyword, Keywords::getKeyword, (t, t1) -> t))
                .values().stream().skip(10).toList();
    }

    public void clearHistory() {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        searchHistoryMapper.delete(Wrappers.lambdaQuery(SearchHistory.class)
                .eq(SearchHistory::getUserId, userId));
    }
}
