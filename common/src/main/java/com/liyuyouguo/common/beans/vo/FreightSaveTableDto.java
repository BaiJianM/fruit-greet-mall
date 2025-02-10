package com.liyuyouguo.common.beans.vo;

import com.liyuyouguo.common.entity.shop.FreightTemplate;
import com.liyuyouguo.common.entity.shop.FreightTemplateGroup;
import lombok.Data;

import java.util.List;

/**
 * @author baijianmin
 */
@Data
public class FreightSaveTableDto {

    private List<FreightTemplateGroup> table;

    private List<FreightTemplateGroup> defaultData;

    private FreightTemplate info;

}
