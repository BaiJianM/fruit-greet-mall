package com.liyuyouguo.common.beans.vo.shop;

import com.liyuyouguo.common.entity.shop.Keywords;
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
