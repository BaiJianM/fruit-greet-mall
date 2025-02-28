package com.liyuyouguo.common.beans.dto.shop;

import com.liyuyouguo.common.entity.shop.FreightTemplateGroup;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author baijianmin
 */
@Data
public class SaveExceptAreaDto {

    private List<FreightTemplateGroup> table;

    private FreightTemplateAddVo info;

    @Data
    public static class FreightTemplateAddVo {

        private Integer id;

        private String name;

        private BigDecimal packagePrice;

        private Integer freightType;

        private Boolean isDelete;

        private String content;

    }

}
