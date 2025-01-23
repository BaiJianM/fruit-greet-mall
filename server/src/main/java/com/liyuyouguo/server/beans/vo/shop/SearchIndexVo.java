package com.liyuyouguo.server.beans.vo.shop;

import com.liyuyouguo.server.entity.shop.Keywords;
import lombok.Data;

import java.util.List;

/**
 * @author baijianmin
 */
@Data
public class SearchIndexVo {

    private Keywords defaultKeyword;

    private List<String> historyKeywordList;

    private List<Keywords> hotKeywordList;

}
