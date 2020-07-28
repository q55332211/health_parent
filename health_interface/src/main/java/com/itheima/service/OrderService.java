package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.pojo.Order;

import java.util.Date;
import java.util.Map;

/**
 * @Description:
 * @Date: Create in 16:38 2020/7/21
 */
public interface OrderService {

    Result sumbit(Map map) throws Exception;

    Integer findByDate(Date date);

    Map<String, Object> getOrderById(Integer id);

    Integer findOrderInfoByCondition(String stratDate, String endtDate, String orderStatus);

    Integer getTotalCount();
    //根据Setmetl ID查询订单数
    Integer getSetmealCountById(Integer id);
}
