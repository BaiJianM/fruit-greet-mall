package com.liyuyouguo.common.beans.enums;

import lombok.Getter;

/**
 * @author baijianmin
 */
@Getter
public enum KdnInterfaceType {

    R2201("2201", "预创建订单"),
    R2202("2202", "创建订单"),
    R2203("2203", "预取消订单"),
    R2204("2204", "取消订单"),
    R2205("2205", "订单信息查询"),
    R2206("2206", "业务员轨迹查询"),
    R2207("2207", "加价"),
    R2208("2208", "加价类型查询"),
    R2211("2211", "创建门店"),
    R2212("2212", "更新门店"),
    R2213("2213", "上传图片"),
    R2214("2214", "创建品牌总部"),
    R2215("2215", "查询配送员轨迹地图"),
    R2218("2218", "查询门店审核状态"),
    R2209("2209", "加价详情查询");
    private String code;
    private String desc;
    KdnInterfaceType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
