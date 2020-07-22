package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;

/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 18:46 2020/7/15
 */
public interface OrderSettingService {

    void add(List<OrderSetting> orderSettingList);

    List<OrderSetting> findOrderSettingByMothe(String date);

    void exitNumberByDate(OrderSetting orderSetting);
}
