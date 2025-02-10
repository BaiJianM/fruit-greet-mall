package com.liyuyouguo.common.beans.vo;

import com.liyuyouguo.common.entity.shop.FreightTemplate;
import com.liyuyouguo.common.entity.shop.FreightTemplateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreightDetailVo {

    private FreightTemplate freight;

    private List<FreightTemplateGroupVo> data;

    private List<FreightTemplateGroup> defaultData;

}
