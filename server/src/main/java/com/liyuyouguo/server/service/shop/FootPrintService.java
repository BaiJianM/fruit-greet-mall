package com.liyuyouguo.server.service.shop;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuyouguo.server.beans.FruitShopPage;
import com.liyuyouguo.server.beans.PageResult;
import com.liyuyouguo.server.beans.vo.shop.FootprintVo;
import com.liyuyouguo.server.entity.shop.Footprint;
import com.liyuyouguo.server.entity.shop.Goods;
import com.liyuyouguo.server.mapper.FootprintMapper;
import com.liyuyouguo.server.mapper.GoodsMapper;
import com.liyuyouguo.server.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 用户足迹服务类
 *
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FootPrintService extends ServiceImpl<FootprintMapper, Footprint> implements IService<Footprint> {

    private final FootprintMapper footprintMapper;

    private final GoodsMapper goodsMapper;

    /**
     * 新增或更新用户足迹
     *
     * @param userId  用户id
     * @param goodsId 商品id
     */
    public void saveFootPrint(Integer userId, Integer goodsId) {
        if (userId > 0 && goodsId > 0) {
            Footprint footPrint = footprintMapper.selectOne(Wrappers.lambdaQuery(Footprint.class)
                    .eq(Footprint::getUserId, userId)
                    .eq(Footprint::getGoodsId, goodsId));
            if (footPrint == null) {
                Footprint newFootprint = new Footprint();
                newFootprint.setUserId(userId);
                newFootprint.setGoodsId(goodsId);
                newFootprint.setAddTime(LocalDateTime.now());
            } else {
                footPrint.setAddTime(LocalDateTime.now());
            }
            this.saveOrUpdate(footPrint);
        }
    }

    /**
     * 删除当天的同一个商品的足迹
     *
     * @param footprintId 足迹id
     * @return String 删除结果
     */
    public String delete(Integer footprintId) {
        // 删除当天的同一个商品的足迹
        int rowsAffected = footprintMapper.delete(Wrappers.lambdaQuery(Footprint.class)
                .eq(Footprint::getId, footprintId));
        if (rowsAffected > 0) {
            return "删除成功";
        } else {
            return "删除失败，记录未找到";
        }
    }

    /**
     * 获取用户足迹
     *
     * @param pageDto 分页参数
     * @return PageResult<Footprint> 用户足迹
     */
    public PageResult<Footprint> getFootprintList(FruitShopPage pageDto) {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        // 分页查询用户的足迹，并关联商品信息
        Page<Footprint> page = new Page<>(pageDto.getCurrent(), pageDto.getSize());
        IPage<Footprint> footprintPage = footprintMapper.getFootprintByUserId(page, userId);
        // 遍历结果列表，查询商品信息并格式化时间
        List<Footprint> records = footprintPage.getRecords();
        List<FootprintVo> dataList = (List<FootprintVo>) ConvertUtils.convertCollection(records, FootprintVo::new,
                (s, t) -> {
                    if (LocalDate.now().equals(s.getAddTime().toLocalDate())) {
                        t.setAddTime("今天");
                    } else {
                        t.setAddTime(s.getAddTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                }).orElseThrow();
        for (FootprintVo vo : dataList) {
            Integer goodsId = vo.getGoodsId();
            // 查询商品信息
            Goods goods = goodsMapper.selectById(goodsId);
            vo.setGoods(goods);
        }
        return ConvertUtils.convert(page, PageResult<Footprint>::new).orElseThrow();
    }
}
