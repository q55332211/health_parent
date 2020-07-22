package com.itheima.dao;

import com.itheima.pojo.Order;

import java.util.Map;

/**
 * @Description:
 * @Date: Create in 16:40 2020/7/21
 */
public interface OrderdDao {

    void add(Map order);

    long findByDate(String date);

   /* 根据用户id以及套餐id查询，防止重复提交*/
    int getOrderByMidAndSid(Map map);
}
