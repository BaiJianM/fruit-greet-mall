package com.liyuyouguo.admin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyuyouguo.common.beans.FruitGreetPage;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.beans.dto.shop.AdDestoryDto;
import com.liyuyouguo.common.beans.dto.shop.AdStoreDto;
import com.liyuyouguo.common.beans.dto.shop.AdUpdateSortDto;
import com.liyuyouguo.common.beans.vo.AdVo;
import com.liyuyouguo.common.entity.shop.Ad;
import com.liyuyouguo.common.entity.shop.Goods;
import com.liyuyouguo.common.mapper.AdMapper;
import com.liyuyouguo.common.mapper.GoodsMapper;
import com.liyuyouguo.common.utils.ConvertUtils;
import com.liyuyouguo.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 广告服务层
 *
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdService {

    private final AdMapper adMapper;

    private final GoodsMapper goodsMapper;

    public PageResult<AdVo> index(FruitGreetPage page) {
        Page<Ad> adPage = adMapper.selectPage(new Page<>(page.getCurrent(), page.getSize()), Wrappers.lambdaQuery(Ad.class)
                .eq(Ad::getIsDelete, 0)
                .orderByAsc(Ad::getId));
        // 处理查询结果
        List<Ad> records = adPage.getRecords();
        List<AdVo> data = (List<AdVo>) ConvertUtils.convertCollection(records, AdVo::new, (s, t) -> t.setEnabled(s.getEnabled() == 1)).orElseThrow();
        return ConvertUtils.convert(adPage, PageResult<AdVo>::new, (s, t) -> t.setRecords(data)).orElseThrow();
    }

    public Integer updateSort(AdUpdateSortDto dto) {
        return adMapper.update(Wrappers.lambdaUpdate(Ad.class)
                .set(Ad::getSortOrder, dto.getSort())
                .eq(Ad::getId, dto.getId()));
    }

    public Ad info(Integer adId) {
        return adMapper.selectById(adId);
    }

    public AdVo store(AdStoreDto dto) {
        Ad updateOrInsertAd = ConvertUtils.convert(dto, Ad::new, (s, t) -> t.setEndTime(DateUtils.parseTime(s.getEndTime()))).orElseThrow();
        if (dto.getId() > 0) {
            adMapper.update(updateOrInsertAd, Wrappers.lambdaUpdate(Ad.class).eq(Ad::getId, dto.getId()));
        } else {
            adMapper.insert(updateOrInsertAd);
        }
        return ConvertUtils.convert(updateOrInsertAd, AdVo::new, (s, t) -> t.setEndTime(s.getEndTime())).orElseThrow();
    }

    public List<Goods> getallrelate() {
        return goodsMapper.selectList(Wrappers.lambdaQuery(Goods.class)
                .eq(Goods::getIsOnSale, 1)
                .eq(Goods::getIsDelete, 0));
    }

    public void destory(AdDestoryDto dto) {
        adMapper.update(Wrappers.lambdaUpdate(Ad.class)
                .set(Ad::getIsDelete, 1)
                .eq(Ad::getId, dto.getId()));
    }

    public void saleStatus(Integer adId, String status) {
        adMapper.update(Wrappers.lambdaUpdate(Ad.class)
                .set(Ad::getEnabled, "true".equals(status) ? 1 : 0)
                .eq(Ad::getId, adId));
    }
}
