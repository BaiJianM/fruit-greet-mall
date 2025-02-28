package com.liyuyouguo.common.beans.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class FreightTemplateGroupVo {

    private Integer id;

    private Integer templateId;

    private Integer isDefault;

    private String area;

    private Integer start;

    private BigDecimal startFee;

    private Integer add;

    private BigDecimal addFee;

    private Boolean freeByNumber;

    private Boolean freeByMoney;

    private Integer isDelete;

    private String areaName;

}
