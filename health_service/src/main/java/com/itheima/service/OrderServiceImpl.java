package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderdDao;

import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @Description:
 * @Date: Create in 16:37 2020/7/21
 */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderdDao orderdDao;

    @Override
    public Result sumbit(Map order) {
        //添加预约人数
        //在订单表添加一条预约信息
        //this.orderdDao.add(order);
        return null;
    }
}
