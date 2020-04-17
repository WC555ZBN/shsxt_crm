package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.OrderDetailsMapper;
import com.shsxt.crm.vo.OrderDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("all")
public class OrderDetailsService extends BaseService<OrderDetails,Integer> {
    @Autowired
    private OrderDetailsMapper orderDetailsMapper;




}
