package com.liyuyouguo.common.beans.dto.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
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

        @JsonProperty("package_price")
        private BigDecimal packagePrice;

        @JsonProperty("freight_type")
        private Integer freightType;

        @JsonProperty("is_delete")
        private Boolean isDelete;

        private String content;

    }

}
