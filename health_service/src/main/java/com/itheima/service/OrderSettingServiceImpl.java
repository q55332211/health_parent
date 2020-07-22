package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 18:49 2020/7/15
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> orderSettingList) {
        if (orderSettingList != null || orderSettingList.size() > 0) {
            for (OrderSetting orderSetting : orderSettingList) {
                //判断日期是否存在
                long count = this.orderSettingDao.findByDate(orderSetting.getOrderDate());
                if (count > 0) {
                    //存在就更新
                    this.orderSettingDao.update(orderSetting);
                } else {
                    //不存在就插入
                    this.orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    @Override
    public List<OrderSetting> findOrderSettingByMothe(String date) {
        String[] split = date.split("-");
        if (split[1].length() <= 1) {
            return this.orderSettingDao.findOrderSettingByMothe(split[0] + "-0" + split[1]);
        }
        return this.orderSettingDao.findOrderSettingByMothe(date);
    }

    @Override
    public void exitNumberByDate(OrderSetting orderSetting) {
        long count = this.orderSettingDao.findByDate(orderSetting.getOrderDate());
        if (count > 0) {
            this.orderSettingDao.update(orderSetting);
            return;
        }
        this.orderSettingDao.add(orderSetting);
    }
}
