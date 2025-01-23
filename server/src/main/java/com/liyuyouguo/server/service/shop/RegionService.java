package com.liyuyouguo.server.service.shop;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liyuyouguo.server.beans.dto.shop.RegionInfoQueryDto;
import com.liyuyouguo.server.beans.dto.shop.RegionQueryDto;
import com.liyuyouguo.server.beans.vo.shop.RegionVo;
import com.liyuyouguo.server.entity.shop.Region;
import com.liyuyouguo.server.mapper.RegionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RegionService {

    private RegionMapper regionMapper;

    public Region getRegion(Integer regionId) {
        return regionMapper.selectById(regionId);
    }

    public List<Region> listRegion(Integer parentId) {
        return regionMapper.selectList(Wrappers.lambdaQuery(Region.class)
                .eq(Region::getParentId, parentId));
    }

    public List<Region> getRegionData(RegionQueryDto dto) {
        return this.listRegion(dto.getParentId());
    }

    public RegionVo getRegionInfo(RegionInfoQueryDto dto) {
        RegionVo regionVo = new RegionVo();
        regionVo.setProvinceId(this.getRegionId(dto.getProvince()));
        regionVo.setCityId(this.getRegionId(dto.getCity()));
        regionVo.setCountryId(this.getRegionId(dto.getCountry()));
        return regionVo;
    }

    private Integer getRegionId(String name) {
        return regionMapper.selectList(Wrappers.lambdaQuery(Region.class)
                .eq(Region::getName, name)).get(0).getId();
    }
}
