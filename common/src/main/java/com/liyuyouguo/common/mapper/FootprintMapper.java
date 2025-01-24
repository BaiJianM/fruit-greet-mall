package com.liyuyouguo.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyuyouguo.common.entity.shop.Footprint;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author baijianmin
 */
@Repository
public interface FootprintMapper extends BaseMapper<Footprint> {

    IPage<Footprint> getFootprintByUserId(Page<Footprint> page, @Param("userId") Integer userId);

}
