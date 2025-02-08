package com.liyuyouguo.common.beans.vo;

import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class DashboardInfoVo {

    private Integer newUser;
    private Integer oldUser;
    private Integer addCart;
    private Integer addOrderNum;
    private Integer addOrderSum;
    private Integer payOrderNum;
    private Integer payOrderSum;
//    private List<User> newData;
//    private List<User> oldData;

}
