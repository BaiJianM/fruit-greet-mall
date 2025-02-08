package com.liyuyouguo.common.beans.vo;

import lombok.Data;

/**
 * @author baijianmin
 */
@Data
public class OrderDetailAdminVo {

    private OrderAdminVo orderInfo;

    private SenderInfoVo sender;

    private ReceiverInfoVo receiver;

}
