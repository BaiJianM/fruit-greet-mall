package com.liyuyouguo.common.beans.vo;

import com.liyuyouguo.common.entity.shop.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardInfoVo {

    private Long newUser;

    private Long oldUser;

    private Long addCart;

    private Long addOrderNum;

    private BigDecimal addOrderSum;

    private Long payOrderNum;

    private BigDecimal payOrderSum;

    private List<User> newData;

    private List<User> oldData;

}
