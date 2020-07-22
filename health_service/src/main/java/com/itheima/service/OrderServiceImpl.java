package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderdDao;

import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.untis.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * @Description:
 * @Date: Create in 16:37 2020/7/21
 */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderdDao orderdDao;
    @Autowired
    private OrderSettingService orderSettingService;
    @Autowired
    private MemberService memberService;

    @Override
    public Result sumbit(Map map) throws Exception {
        ///查询日期是否可以约  查询之前要进行日期转换
        String dateString = map.get("orderDate").toString();
        String[] strings = dateString.split("T");
        //todo 可能需要怼stringDate进行切割
        Date date = DateUtils.parseString2Date(strings[0]);
        Integer number = this.orderSettingService.getNumberByDate(date);
        Integer reservations = this.orderSettingService.getReservationsByDate(date);
        if (reservations < number) {
            //查询用户是否已经注册
            Member member = this.memberService.findByPhone((String) map.get("telephone"));
            if (member == null) {
                // 未注册 自动注册添加信息到用户表 然后创建 order //todo sql未写
                Integer memberId = this.memberService.add(map);
                member.setId(memberId);
            }
            //防止重复提交
            map.put("memberId", member.getId());
            int conut = this.orderdDao.getOrderByMidAndSid(map);
            if (conut <= 0) {

            }
            //已经注册直接创建订单order

            //在订单表添加一条预约信息

            this.orderdDao.add(map);
            //this.orderdDao.add(order);

            //添加预约人数
            this.orderSettingService.exitReservationsByDate(date);
        }

        return null;
    }

    @Override
    public boolean findByDate(String date) {
        long count = this.orderdDao.findByDate(date);
        return 1 == count;
    }

}
