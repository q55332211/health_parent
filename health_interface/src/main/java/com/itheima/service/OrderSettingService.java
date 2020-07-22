package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;

/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 18:46 2020/7/15
 */
public interface OrderSettingService {

    void add(List<OrderSetting> orderSettingList);

    //根据月份查询
    List<OrderSetting> findOrderSettingByMothe(String date);

    void exitNumberByDate(OrderSetting orderSetting);

    void exitReservationsByDate(Date date);

    Integer getNumberByDate(Date date);

    Integer getReservationsByDate(Date date);
}
