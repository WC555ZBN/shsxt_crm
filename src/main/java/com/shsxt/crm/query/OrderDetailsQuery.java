package com.shsxt.crm.query;

import com.shsxt.base.BaseQuery;

public class OrderDetailsQuery extends BaseQuery {

    //订单id
    private Integer orderId;
    private String goodName;
    //价格区间
    private Integer type;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
