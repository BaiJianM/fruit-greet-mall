package com.liyuyouguo.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liyuyouguo.common.beans.vo.WapIndexVo;
import com.liyuyouguo.common.entity.shop.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 *
 * @author baijianmin
 */
@Repository
public interface ProductMapper extends BaseMapper<Product> {

    List<WapIndexVo> getProductInfoList();

}
