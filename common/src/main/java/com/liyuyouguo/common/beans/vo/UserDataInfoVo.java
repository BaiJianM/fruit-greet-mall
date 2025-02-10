package com.liyuyouguo.common.beans.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author baijianmin
 */
@Data
public class UserDataInfoVo {

    private Long orderSum;

    private Long orderDone;

    private BigDecimal orderMoney;

    private Long cartSum;

}
