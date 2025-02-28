package com.liyuyouguo.common.beans.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author baijianmin
 */
@Data
public class KeywordsCartVo {

    private Integer id;

    private Integer userId;

    private Integer goodsId;

    private String goodsSn;

    private Integer productId;

    private String goodsName;

    private String goodsAka;

    private Double goodsWeight;

    private BigDecimal addPrice;

    private BigDecimal retailPrice;

    private Integer number;

    private String goodsSpecifitionNameValue;

    private String goodsSpecifitionIds;

    private Integer checked;

    private String listPicUrl;

    private Integer freightTemplateId;

    private Integer isOnSale;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addTime;

    private Integer isFast;

    private Integer isDelete;

}
