package com.liyuyouguo.common.beans.vo;

import com.liyuyouguo.common.entity.shop.Goods;
import lombok.Data;

import java.util.List;

/**
 * @author baijianmin
 */
@Data
public class GoodsInfoAdminVo {

    private Goods info;

    private List<Integer> cateData;

}
