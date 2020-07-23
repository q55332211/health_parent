package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;

/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 18:52 2020/7/15
 */
public interface OrderSettingDao {

    Long findByDate(Date date);

    void add(OrderSetting orderSetting);

    void update(OrderSetting orderSetting);

    List<OrderSetting> findOrderSettingByMothe(String date);

    void exitOrderSetting(OrderSetting orderSetting);

    void exitReservationsByDate(Date date);

    Integer getNumberByDate(Date date);

    Integer getReservationsByDate(Date date);

    OrderSetting getOrderSettingByDate(Date date);

}
