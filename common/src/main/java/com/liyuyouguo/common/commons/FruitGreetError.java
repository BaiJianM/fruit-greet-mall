package com.liyuyouguo.common.commons;

/**
 * 业务异常枚举类
 *
 * @author baijianmin
 */
public enum FruitGreetError implements ErrorResponse<Integer> {

    // 业务相关异常提示
    INDEX_ERROR(1001, "首页分类数据转换失败"),
    WECHAT_LOGIN_ERROR(1002, "微信登录失败"),
    GOODS_NOT_EXIST(1003, "该商品不存在或已下架"),
    SKU_ERROR(1004, "规格信息转换失败"),
    CART_EMPTY(1005, "购物车为空"),
    INSUFFICIENT_STOCK(1006, "库存不足"),
    ITEM_NOT_AVAILABLE(1007, "商品已下架"),
    CART_ITEM_NOT_EXIST(1008, "购物车商品不存在"),
    INVALID_CART_OPERATION(1009, "不支持的购物车操作"),
    PRODUCT_ID_NOT_EMPTY(1010, "商品id不能为空"),
    ORDER_NOT_EXIST(1011, "订单不存在"),
    ORDER_CANNOT_CANCEL(1012, "订单不能取消"),
    ORDER_CANNOT_DELETE(1013, "订单不能删除"),
    ORDER_CANNOT_CONFIRM(1014, "订单不能确认"),
    NOT_HAVE_ADDRESS(1015, "请选择收货地址"),
    NOT_HAVE_GOODS(1016, "请选择商品"),
    INSUFFICIENT_STOCK_REORDER(1017, "库存不足，请重新下单"),
    PRICE_CHANGED_REORDER(1018, "价格发生变化，请重新下单"),
    ORDER_SUBMIT_ERROR(1019, "订单提交失败"),
    NO_EXPRESS_DATA(1020, "暂无物流信息"),
    NO_EXPRESS(1021, "暂无物流信息【1】"),
    QUERY_EXPRESS_ERROR(1022, "查询物流信息失败"),
    NAME_EXIST(1023, "重名了"),
    GOODS_EMPTY(1024, "商品不存在"),
    GOODS_LEAST_ONE(1025, "商品的规格数量至少1个"),
    SKU_REPEAT(1026, "规格重复"),
    GOODS_SN_REPEAT(1027, "商品编号重复"),
    SKU_GOODS_EXIST(1028, "该规格下有商品，暂不能删除"),
    GOODS_HAS_AD(1029, "该商品已经有广告关联"),
    ;

    /**
     * 异常状态码
     */
    private final Integer code;

    /**
     * 异常描述
     */
    private final String describe;

    FruitGreetError(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDescribe() {
        return describe;
    }


}
