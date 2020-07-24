package com.itheima.dao;

import com.itheima.pojo.Order;

import java.util.Map;

/**
 * @Description:
 * @Date: Create in 16:40 2020/7/21
 */
public interface OrderDao {

    void add(Order order);

    long findByDate(String date);

    /* 根据用户id以及套餐id查询，防止重复提交*/
    long getOrder(Order order);

    /*根据id查询订单*/
    Map<String, Object> getOrderById(Integer id);
}
