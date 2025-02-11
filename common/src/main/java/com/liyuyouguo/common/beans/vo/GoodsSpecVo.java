package com.liyuyouguo.common.beans.vo;

import com.liyuyouguo.common.entity.shop.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSpecVo {

    private List<GoodsSpecDataVo> specData;

    private Integer specValue;

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class GoodsSpecDataVo extends Product {

        private String value;

    }

}
